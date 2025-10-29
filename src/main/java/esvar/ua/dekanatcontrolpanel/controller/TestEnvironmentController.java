package esvar.ua.dekanatcontrolpanel.controller;

import esvar.ua.dekanatcontrolpanel.dto.TestEnvironmentStatusDto;
import esvar.ua.dekanatcontrolpanel.dto.TestEnvironmentStatusResponse;
import esvar.ua.dekanatcontrolpanel.security.SecurityUtils;
import esvar.ua.dekanatcontrolpanel.service.TestEnvironmentService;
import esvar.ua.dekanatcontrolpanel.service.shell.ShellCommandResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/test", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestEnvironmentController {

    private static final int RESPONSE_LOG_LIMIT = 200;

    private final TestEnvironmentService testEnvironmentService;

    public TestEnvironmentController(TestEnvironmentService testEnvironmentService) {
        this.testEnvironmentService = testEnvironmentService;
    }

    @GetMapping("/status")
    @PreAuthorize("isAuthenticated()")
    public TestEnvironmentStatusDto status() {
        return testEnvironmentService.getStatus();
    }

    @PostMapping("/start")
    @PreAuthorize("hasAnyRole('ADMIN','TEST_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommandResponse startEnvironment() {
        ShellCommandResult result = testEnvironmentService.startEnvironment();
        testEnvironmentService.start(SecurityUtils.getCurrentUserEmailOrThrow());
        return toResponse(result);
    }

    @PostMapping("/stop")
    @PreAuthorize("hasAnyRole('ADMIN','TEST_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommandResponse stopEnvironment() {
        ShellCommandResult result = testEnvironmentService.stopEnvironment();
        testEnvironmentService.stop(SecurityUtils.getCurrentUserEmailOrThrow());
        return toResponse(result);
    }

    @PostMapping("/restart")
    @PreAuthorize("hasAnyRole('ADMIN','TEST_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommandResponse startEnvironment() {
        String email = SecurityUtils.getCurrentUserEmailOrThrow();
        ShellCommandResult result = testEnvironmentService.startEnvironment(email);
        return toResponse(result);
    }

    @PostMapping("/stop")
    @PreAuthorize("hasAnyRole('ADMIN','TEST_MANAGER')")
    public CommandResponse stopEnvironment() {
        String email = SecurityUtils.getCurrentUserEmailOrThrow();
        ShellCommandResult result = testEnvironmentService.stopEnvironment(email);
        return toResponse(result);
    }

    @PostMapping("/restart")
    @PreAuthorize("hasAnyRole('ADMIN','TEST_MANAGER')")
    public CommandResponse restartEnvironment() {
        String email = SecurityUtils.getCurrentUserEmailOrThrow();
        ShellCommandResult result = testEnvironmentService.restartEnvironment(email);
        return toResponse(result);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN','TEST_MANAGER')")
    public CommandResponse updateEnvironment() {
        String email = SecurityUtils.getCurrentUserEmailOrThrow();
        ShellCommandResult result = testEnvironmentService.updateEnvironment(email);
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
