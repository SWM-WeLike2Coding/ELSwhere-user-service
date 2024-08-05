package com.wl2c.elswhereuserservice.domain.user.exception;

import com.wl2c.elswhereuserservice.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class AlreadyHoldingException extends LocalizedMessageException {
    public AlreadyHoldingException() {
        super(HttpStatus.BAD_REQUEST, "already.holding");
    }
}
