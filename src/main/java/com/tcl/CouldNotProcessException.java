package com.tcl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CouldNotProcessException extends RuntimeException {

    private String errors;

    public CouldNotProcessException(String message, String errors, Exception e) {
        super(message);
        this.errors = errors;
        if (e != null) {
            this.initCause(e);
        }
    }

    public CouldNotProcessException(String message) {
        super(message);
    }


    public String getErrors() {
        return errors;
    }
}