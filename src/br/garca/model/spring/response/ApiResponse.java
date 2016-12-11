package br.garca.model.spring.response;

public class ApiResponse {

    private boolean ok;
    private String message;

    public ApiResponse() {
        super();
    }

    public ApiResponse(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
