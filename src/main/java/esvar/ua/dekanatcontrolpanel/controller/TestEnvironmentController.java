package esvar.ua.dekanatcontrolpanel.controller;

import esvar.ua.dekanatcontrolpanel.dto.TestEnvironmentStatusDto;
import esvar.ua.dekanatcontrolpanel.service.TestEnvironmentService;
import esvar.ua.dekanatcontrolpanel.service.shell.ShellCommandResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/test", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestEnvironmentController {

    private static final int RESPONSE_LOG_LIMIT = 200;

    private final TestEnvironmentService testEnvironmentService;

    public TestEnvironmentController(TestEnvironmentService testEnvironmentService) {
        this.testEnvironmentService = testEnvironmentService;
    }

    @GetMapping("/status")
    public TestEnvironmentStatusDto getStatus() {
        return testEnvironmentService.getStatus();
    }

    @PostMapping("/start")
    public CommandResponse startEnvironment() {
        ShellCommandResult result = testEnvironmentService.startEnvironment();
        return toResponse(result);
    }

    @PostMapping("/stop")
    public CommandResponse stopEnvironment() {
        ShellCommandResult result = testEnvironmentService.stopEnvironment();
        return toResponse(result);
    }

    @PostMapping("/restart")
    public CommandResponse restartEnvironment() {
        ShellCommandResult result = testEnvironmentService.restartEnvironment();
        return toResponse(result);
    }

    @PostMapping("/update")
    public CommandResponse updateEnvironment(@RequestBody(required = false) UpdateRequest request) {
        ShellCommandResult result = testEnvironmentService.updateEnvironment(request != null ? request.getTriggeredBy() : null);
        return toResponse(result);
    }

    private CommandResponse toResponse(ShellCommandResult result) {
        boolean success = result.getExitCode() == 0;
        String log = truncate(result.getTruncatedLog(), RESPONSE_LOG_LIMIT);
        return new CommandResponse(success, log);
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }

    public static class UpdateRequest {
        private String triggeredBy;

        public String getTriggeredBy() {
            return triggeredBy;
        }

        public void setTriggeredBy(String triggeredBy) {
            this.triggeredBy = triggeredBy;
        }
    }

    public static class CommandResponse {
        private final boolean success;
        private final String log;

        public CommandResponse(boolean success, String log) {
            this.success = success;
            this.log = log;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getLog() {
            return log;
        }
    }
}
