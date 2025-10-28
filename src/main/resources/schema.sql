CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(32) NOT NULL,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS deployment_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    env VARCHAR(255) NOT NULL,
    version_before VARCHAR(255),
    version_after VARCHAR(255),
    started_at TIMESTAMP NOT NULL,
    finished_at TIMESTAMP,
    status VARCHAR(32) NOT NULL,
    triggered_by VARCHAR(255),
    log_excerpt TEXT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS error_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    env VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    severity VARCHAR(32) NOT NULL,
    short_message VARCHAR(255) NOT NULL,
    full_message MEDIUMTEXT,
    related_deployment_id BIGINT,
    CONSTRAINT fk_error_log_deployment FOREIGN KEY (related_deployment_id)
        REFERENCES deployment_history (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS audit_event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_email VARCHAR(255) NOT NULL,
    action_type VARCHAR(255) NOT NULL,
    target_env VARCHAR(255),
    timestamp TIMESTAMP NOT NULL,
    status VARCHAR(32) NOT NULL,
    message VARCHAR(255)
) ENGINE=InnoDB;
