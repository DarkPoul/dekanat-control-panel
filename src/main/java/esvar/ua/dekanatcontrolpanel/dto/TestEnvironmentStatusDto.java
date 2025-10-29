package esvar.ua.dekanatcontrolpanel.dto;

public class TestEnvironmentStatusDto {

    private final boolean running;
    private final String version;
    private final String uptime;
    private final String health;
    private final String lastUpdateInfo;

    public TestEnvironmentStatusDto(boolean running, String version, String uptime, String health, String lastUpdateInfo) {
        this.running = running;
        this.version = version;
        this.uptime = uptime;
        this.health = health;
        this.lastUpdateInfo = lastUpdateInfo;
    }

    public boolean isRunning() {
        return running;
    }

    public String getVersion() {
        return version;
    }

    public String getUptime() {
        return uptime;
    }

    public String getHealth() {
        return health;
    }

    public String getLastUpdateInfo() {
        return lastUpdateInfo;
    }
}
