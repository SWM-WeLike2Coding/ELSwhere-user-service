package com.wl2c.elswhereuserservice.domain.user.exception;

import com.wl2c.elswhereuserservice.global.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InterestNotFoundException extends LocalizedMessageException {
    public InterestNotFoundException() { super(HttpStatus.NOT_FOUND, "notfound.interest-product"); }
}
