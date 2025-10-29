package esvar.ua.dekanatcontrolpanel.dto;

import esvar.ua.dekanatcontrolpanel.entity.ErrorLog;
import java.time.Instant;

public class ErrorLogSummaryDto {

    private final Long id;
    private final Instant createdAt;
    private final String env;
    private final ErrorLog.Severity severity;
    private final String shortMessage;

    public ErrorLogSummaryDto(Long id, Instant createdAt, String env, ErrorLog.Severity severity, String shortMessage) {
        this.id = id;
        this.createdAt = createdAt;
        this.env = env;
        this.severity = severity;
        this.shortMessage = shortMessage;
    }

    public Long getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getEnv() {
        return env;
    }

    public ErrorLog.Severity getSeverity() {
        return severity;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public static ErrorLogSummaryDto fromEntity(ErrorLog errorLog) {
        return new ErrorLogSummaryDto(
                errorLog.getId(),
                errorLog.getCreatedAt(),
                errorLog.getEnv(),
                errorLog.getSeverity(),
                errorLog.getShortMessage()
        );
    }
}