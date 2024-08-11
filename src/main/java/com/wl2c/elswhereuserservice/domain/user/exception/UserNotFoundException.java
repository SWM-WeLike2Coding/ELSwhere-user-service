package com.wl2c.elswhereuserservice.domain.user.exception;

import com.wl2c.elswhereuserservice.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends LocalizedMessageException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.user");
    }
}