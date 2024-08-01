package com.wl2c.elswhereuserservice.domain.user.exception;

import com.wl2c.elswhereuserservice.global.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class AlreadyInterestException extends LocalizedMessageException {
    public AlreadyInterestException() {
        super(HttpStatus.BAD_REQUEST, "already.interest");
    }
}
