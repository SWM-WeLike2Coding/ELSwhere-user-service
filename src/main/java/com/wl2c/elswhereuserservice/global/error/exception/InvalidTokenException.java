package com.wl2c.elswhereuserservice.global.error.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends LocalizedMessageException {
    public InvalidTokenException() {
        super(HttpStatus.UNAUTHORIZED, "invalid.token");
    }
}
