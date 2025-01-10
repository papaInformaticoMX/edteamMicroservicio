package net.viancom.api_reservations.exception;

import net.viancom.api_reservations.enums.APIError;
import org.springframework.http.HttpStatus;

import java.util.List;

public class EdteamException extends RuntimeException {
    private HttpStatus status;
    private String description;
    private List<String> reasons;

    public HttpStatus getStatus() {
        return status;
    }

    public EdteamException(APIError error) {
        this.status = error.getHttpStatus();
        this.description = error.getMessage();
    }


    public EdteamException(String message, HttpStatus status, String description, List<String> reasons) {
        //super(message);
        this.status = status;
        this.description = description;
        this.reasons = reasons;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }


    /*

    public EdteamException(String message) {
        super(message);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }*/
}