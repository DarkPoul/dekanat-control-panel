package esvar.ua.dekanatcontrolpanel.controller;

import esvar.ua.dekanatcontrolpanel.dto.ErrorLogDetailsDto;
import esvar.ua.dekanatcontrolpanel.dto.ErrorLogPageDto;
import esvar.ua.dekanatcontrolpanel.dto.ErrorLogSummaryDto;
import esvar.ua.dekanatcontrolpanel.entity.ErrorLog;
import esvar.ua.dekanatcontrolpanel.service.ErrorLogService;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/logs")
public class LogsController {

    private final ErrorLogService errorLogService;

    public LogsController(ErrorLogService errorLogService) {
        this.errorLogService = errorLogService;
    }

    @GetMapping("/recent")
    public List<ErrorLogSummaryDto> getRecentLogs(@RequestParam(name = "limit", defaultValue = "5") int limit) {
        if (limit <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "limit must be greater than 0");
        }
        return errorLogService.getRecentLogs(limit);
    }

    @GetMapping
    public ErrorLogPageDto getLogs(
            @RequestParam(name = "env", required = false) String env,
            @RequestParam(name = "severity", required = false) String severity,
            @RequestParam(name = "from", required = false) String from,
            @RequestParam(name = "to", required = false) String to,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize
    ) {
        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "page must be greater or equal to 0");
        }
        if (pageSize <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "pageSize must be greater than 0");
        }

        String normalizedEnv = normalizeEnv(env);
        ErrorLog.Severity parsedSeverity = parseSeverity(severity);
        Instant fromInstant = parseInstant("from", from);
        Instant toInstant = parseInstant("to", to);

        if (fromInstant != null && toInstant != null && fromInstant.isAfter(toInstant)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "from must be before to");
        }

        return errorLogService.getLogs(normalizedEnv, parsedSeverity, fromInstant, toInstant, page, pageSize);
    }

    @GetMapping("/{id}")
    public ErrorLogDetailsDto getLogDetails(@PathVariable("id") Long id) {
        return errorLogService.getLogDetails(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Log entry not found"));
    }

    private String normalizeEnv(String env) {
        if (env == null) {
            return null;
        }
        String trimmed = env.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private ErrorLog.Severity parseSeverity(String severity) {
        if (severity == null || severity.isBlank()) {
            return null;
        }
        try {
            return ErrorLog.Severity.valueOf(severity.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid severity value: " + severity);
        }
    }

    private Instant parseInstant(String field, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Instant.parse(value.trim());
        } catch (DateTimeParseException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid " + field + " date format");
        }
    }
}