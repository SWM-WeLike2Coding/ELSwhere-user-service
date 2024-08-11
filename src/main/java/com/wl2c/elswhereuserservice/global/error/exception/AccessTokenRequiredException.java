package com.wl2c.elswhereuserservice.global.error.exception;

import org.springframework.http.HttpStatus;

public class AccessTokenRequiredException extends LocalizedMessageException {
    public AccessTokenRequiredException() {
        super(HttpStatus.UNAUTHORIZED, "required.access-token");
    }
}
