package esvar.ua.dekanatcontrolpanel.service.shell;

public class ShellCommandResult {

    private final int exitCode;
    private final String truncatedLog;

    public ShellCommandResult(int exitCode, String truncatedLog) {
        this.exitCode = exitCode;
        this.truncatedLog = truncatedLog;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getTruncatedLog() {
        return truncatedLog;
    }

    public boolean isSuccess() {
        return exitCode == 0;
    }
}
