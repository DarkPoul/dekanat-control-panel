package esvar.ua.dekanatcontrolpanel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Set;

@ConfigurationProperties(prefix = "environments.test")
public class TestEnvironmentProperties {

    private Scripts scripts = new Scripts();
    private URI healthUrl;

    public Scripts getScripts() {
        return scripts;
    }

    public void setScripts(Scripts scripts) {
        this.scripts = scripts;
    }

    public URI getHealthUrl() {
        return healthUrl;
    }

    public void setHealthUrl(URI healthUrl) {
        this.healthUrl = healthUrl;
    }

    public Set<String> getAllowedScripts() {
        Set<String> values = new LinkedHashSet<>();
        if (scripts != null) {
            scripts.addIfPresent(values, scripts.getUpdate());
            scripts.addIfPresent(values, scripts.getStart());
            scripts.addIfPresent(values, scripts.getStop());
            scripts.addIfPresent(values, scripts.getRestart());
            scripts.addIfPresent(values, scripts.getStatus());
        }
        return Set.copyOf(values);
    }

    public static class Scripts {
        private String update;
        private String start;
        private String stop;
        private String restart;
        private String status;

        public String getUpdate() {
            return update;
        }

        public void setUpdate(String update) {
            this.update = update;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getStop() {
            return stop;
        }

        public void setStop(String stop) {
            this.stop = stop;
        }

        public String getRestart() {
            return restart;
        }

        public void setRestart(String restart) {
            this.restart = restart;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        private void addIfPresent(Set<String> target, String value) {
            if (StringUtils.hasText(value)) {
                target.add(value);
            }
        }
    }
}
