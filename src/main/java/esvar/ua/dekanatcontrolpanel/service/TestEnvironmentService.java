package esvar.ua.dekanatcontrolpanel.service;

import esvar.ua.dekanatcontrolpanel.controller.dto.TestEnvironmentStatusResponse;
import esvar.ua.dekanatcontrolpanel.entity.AuditEvent;
import esvar.ua.dekanatcontrolpanel.repo.AuditEventRepository;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class TestEnvironmentService {

    private static final String TEST_ENVIRONMENT = "TEST";

    private final AuditEventRepository auditEventRepository;

    public TestEnvironmentService(AuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }

    public TestEnvironmentStatusResponse getStatus() {
        return new TestEnvironmentStatusResponse("UNKNOWN", "Status provider not implemented yet");
    }

    public void start(String triggeredByEmail) {
        recordAudit(triggeredByEmail, "TEST_START");
    }

    public void stop(String triggeredByEmail) {
        recordAudit(triggeredByEmail, "TEST_STOP");
    }

    public void restart(String triggeredByEmail) {
        recordAudit(triggeredByEmail, "TEST_RESTART");
    }

    public void update(String triggeredByEmail) {
        recordAudit(triggeredByEmail, "TEST_UPDATE");
    }

    private void recordAudit(String triggeredByEmail, String actionType) {
        if (triggeredByEmail == null || triggeredByEmail.isBlank()) {
            throw new IllegalArgumentException("triggeredByEmail must not be blank");
        }
        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setUserEmail(triggeredByEmail);
        auditEvent.setActionType(actionType);
        auditEvent.setTargetEnv(TEST_ENVIRONMENT);
        auditEvent.setTimestamp(Instant.now());
        auditEvent.setStatus(AuditEvent.Status.SUCCESS);
        auditEventRepository.save(auditEvent);
    }
}
