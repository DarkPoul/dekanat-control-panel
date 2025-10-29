package esvar.ua.dekanatcontrolpanel.service;

import esvar.ua.dekanatcontrolpanel.config.TestEnvironmentProperties;
import esvar.ua.dekanatcontrolpanel.dto.TestEnvironmentStatusDto;
import esvar.ua.dekanatcontrolpanel.dto.TestEnvironmentStatusResponse;
import esvar.ua.dekanatcontrolpanel.entity.AuditEvent;
import esvar.ua.dekanatcontrolpanel.repo.AuditEventRepository;
import esvar.ua.dekanatcontrolpanel.service.shell.ShellCommandResult;
import esvar.ua.dekanatcontrolpanel.service.shell.ShellCommandRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TestEnvironmentService {

    private static final Pattern VERSION_PATTERN = Pattern.compile("version\\s*[:=]\\s*([^\\s]+)", Pattern.CASE_INSENSITIVE);

    private static final String TEST_ENVIRONMENT = "TEST";

    private final AuditEventRepository auditEventRepository;

    private final ShellCommandRunner commandRunner;
    private final TestEnvironmentProperties properties;
    private final RestTemplate restTemplate;

    public TestEnvironmentService(AuditEventRepository auditEventRepository, ShellCommandRunner commandRunner,
                                  TestEnvironmentProperties properties,
                                  RestTemplateBuilder restTemplateBuilder) {
        this.auditEventRepository = auditEventRepository;
        this.commandRunner = commandRunner;
        this.properties = properties;
        this.restTemplate = restTemplateBuilder.build();
    }

    public TestEnvironmentStatusDto getStatus() {
        String statusScript = requireScript("status", properties.getScripts().getStatus());
        ShellCommandResult statusResult = commandRunner.execute(List.of(statusScript));

        boolean running = determineRunning(statusResult);
        String version = extractVersion(statusResult.getTruncatedLog());
        String uptime = extractUptime(statusResult.getTruncatedLog());
        String health = determineHealth();

        return new TestEnvironmentStatusDto(running, version, uptime, health, "N/A");
    }

    public ShellCommandResult startEnvironment(String triggeredByEmail) {
        String script = requireScript("start", properties.getScripts().getStart());
        return executeManagedScript(triggeredByEmail, script, "TEST_START");
    }

    public ShellCommandResult stopEnvironment(String triggeredByEmail) {
        String script = requireScript("stop", properties.getScripts().getStop());
        return executeManagedScript(triggeredByEmail, script, "TEST_STOP");
    }

    public ShellCommandResult restartEnvironment(String triggeredByEmail) {
        String script = requireScript("restart", properties.getScripts().getRestart());
        return executeManagedScript(triggeredByEmail, script, "TEST_RESTART");
    }

    public ShellCommandResult updateEnvironment(String triggeredByEmail) {
        String script = requireScript("update", properties.getScripts().getUpdate());
        return executeManagedScript(triggeredByEmail, script, "TEST_UPDATE");
    }

    private boolean determineRunning(ShellCommandResult result) {
        if (!result.isSuccess()) {
            return false;
        }
        String log = result.getTruncatedLog();
        if (log == null) {
            return result.isSuccess();
        }
        log = log.toLowerCase(Locale.ROOT);
        if (log.contains("not running") || log.contains("stopped")) {
            return false;
        }
        return log.contains("running") || log.contains("started");
    }

    private String extractVersion(String log) {
        if (log == null) {
            return "N/A";
        }
        Matcher matcher = VERSION_PATTERN.matcher(log);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "N/A";
    }

    private String extractUptime(String log) {
        if (log == null) {
            return "N/A";
        }
        return log.lines()
                .filter(line -> line.toLowerCase(Locale.ROOT).contains("uptime"))
                .findFirst()
                .orElseGet(() -> StringUtils.hasText(log) ? log : "N/A");
    }

    private String determineHealth() {
        URI healthUrl = properties.getHealthUrl();
        if (healthUrl == null) {
            return "UNKNOWN";
        }

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(healthUrl, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                String body = response.getBody();
                if (body != null && body.contains("UP")) {
                    return "OK";
                }
            }
            return "DOWN";
        } catch (RestClientException ex) {
            return "DOWN";
        }
    }

    private String requireScript(String name, String value) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalStateException("Script path for '" + name + "' is not configured");
        }
        return value;
    }

    private ShellCommandResult executeManagedScript(String triggeredByEmail, String scriptPath, String actionType) {
        String email = normalizeEmail(triggeredByEmail);
        ShellCommandResult result = runScript(scriptPath);
        recordAudit(email, actionType, result);
        return result;
    }

    private ShellCommandResult runScript(String scriptPath) {
        return commandRunner.execute(List.of(scriptPath));
    }

    private String normalizeEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("triggeredByEmail must not be blank");
        }
        return email.trim();
    }



    private void recordAudit(String triggeredByEmail, String actionType, ShellCommandResult result) {
        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setUserEmail(triggeredByEmail);
        auditEvent.setActionType(actionType);
        auditEvent.setTargetEnv(TEST_ENVIRONMENT);
        auditEvent.setTimestamp(Instant.now());
        auditEvent.setStatus(result.isSuccess() ? AuditEvent.Status.SUCCESS : AuditEvent.Status.FAILED);
        auditEventRepository.save(auditEvent);
    }


}
