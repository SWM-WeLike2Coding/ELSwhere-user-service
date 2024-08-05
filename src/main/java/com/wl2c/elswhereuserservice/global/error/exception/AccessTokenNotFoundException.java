package com.wl2c.elswhereuserservice.global.error.exception;

import org.springframework.http.HttpStatus;

public class AccessTokenNotFoundException extends LocalizedMessageException {
    public AccessTokenNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.access-token");
    }
}
