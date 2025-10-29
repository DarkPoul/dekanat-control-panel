package esvar.ua.dekanatcontrolpanel.dto;

import esvar.ua.dekanatcontrolpanel.entity.ErrorLog;
import java.time.Instant;

public class ErrorLogDetailsDto {

    private final Long id;
    private final Instant createdAt;
    private final String env;
    private final ErrorLog.Severity severity;
    private final String shortMessage;
    private final String fullMessage;
    private final Long relatedDeploymentId;

    public ErrorLogDetailsDto(Long id, Instant createdAt, String env, ErrorLog.Severity severity,
                              String shortMessage, String fullMessage, Long relatedDeploymentId) {
        this.id = id;
        this.createdAt = createdAt;
        this.env = env;
        this.severity = severity;
        this.shortMessage = shortMessage;
        this.fullMessage = fullMessage;
        this.relatedDeploymentId = relatedDeploymentId;
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

    public String getFullMessage() {
        return fullMessage;
    }

    public Long getRelatedDeploymentId() {
        return relatedDeploymentId;
    }

    public static ErrorLogDetailsDto fromEntity(ErrorLog errorLog) {
        Long deploymentId = errorLog.getRelatedDeployment() != null
                ? errorLog.getRelatedDeployment().getId()
                : null;

        return new ErrorLogDetailsDto(
                errorLog.getId(),
                errorLog.getCreatedAt(),
                errorLog.getEnv(),
                errorLog.getSeverity(),
                errorLog.getShortMessage(),
                errorLog.getFullMessage(),
                deploymentId
        );
    }
}