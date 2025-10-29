package esvar.ua.dekanatcontrolpanel.dto;

public class TestEnvironmentStatusResponse {

    private final String status;
    private final String details;

    public TestEnvironmentStatusResponse(String status, String details) {
        this.status = status;
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }
}
