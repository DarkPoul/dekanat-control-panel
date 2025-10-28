package ua.ntu.controlpanel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "deployment_history")
public class DeploymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String env;

    @Column(name = "version_before", length = 255)
    private String versionBefore;

    @Column(name = "version_after", length = 255)
    private String versionAfter;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private Status status;

    @Column(name = "triggered_by", length = 255)
    private String triggeredBy;

    @Lob
    @Column(name = "log_excerpt")
    private String logExcerpt;

    public enum Status {
        IN_PROGRESS,
        SUCCESS,
        FAILED,
        TIMEOUT
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getVersionBefore() {
        return versionBefore;
    }

    public void setVersionBefore(String versionBefore) {
        this.versionBefore = versionBefore;
    }

    public String getVersionAfter() {
        return versionAfter;
    }

    public void setVersionAfter(String versionAfter) {
        this.versionAfter = versionAfter;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public String getLogExcerpt() {
        return logExcerpt;
    }

    public void setLogExcerpt(String logExcerpt) {
        this.logExcerpt = logExcerpt;
    }
}
