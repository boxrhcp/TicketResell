package eit.tub.ec.TicketResellBackend.Utils;

import org.springframework.http.HttpStatus;

public class APIError {
    private HttpStatus error;
    private String message;

    public APIError() {}

    public APIError(HttpStatus error, String message) {
        this.error = error;
        this.message = message;
    }

    public HttpStatus getError() {
        return error;
    }

    public void setError(HttpStatus error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
