package dto;

public class TransaccionResponse {

    private String message;
    private Integer status;
    private String error;
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public TransaccionResponse(String message, Integer status, String error) {
        this.message = message;
        this.status = status;
        this.error = error;
    }

    public TransaccionResponse() {
    }
}
