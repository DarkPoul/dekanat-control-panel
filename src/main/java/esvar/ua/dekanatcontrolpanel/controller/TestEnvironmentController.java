package esvar.ua.dekanatcontrolpanel.controller;

import esvar.ua.dekanatcontrolpanel.controller.dto.TestEnvironmentStatusResponse;
import esvar.ua.dekanatcontrolpanel.security.SecurityUtils;
import esvar.ua.dekanatcontrolpanel.service.TestEnvironmentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/api/test")
public class TestEnvironmentController {

    private final TestEnvironmentService testEnvironmentService;

    public TestEnvironmentController(TestEnvironmentService testEnvironmentService) {
        this.testEnvironmentService = testEnvironmentService;
    }

    @GetMapping("/status")
    @PreAuthorize("isAuthenticated()")
    public TestEnvironmentStatusResponse status() {
        return testEnvironmentService.getStatus();
    }

    @PostMapping("/start")
    @PreAuthorize("hasAnyRole('ADMIN','TEST_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void start() {
        testEnvironmentService.start(SecurityUtils.getCurrentUserEmailOrThrow());
    }

    @PostMapping("/stop")
    @PreAuthorize("hasAnyRole('ADMIN','TEST_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void stop() {
        testEnvironmentService.stop(SecurityUtils.getCurrentUserEmailOrThrow());
    }

    @PostMapping("/restart")
    @PreAuthorize("hasAnyRole('ADMIN','TEST_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void restart() {
        testEnvironmentService.restart(SecurityUtils.getCurrentUserEmailOrThrow());
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN','TEST_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update() {
        testEnvironmentService.update(SecurityUtils.getCurrentUserEmailOrThrow());
    }
}
