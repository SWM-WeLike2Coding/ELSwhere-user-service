package com.wl2c.elswhereuserservice.domain.user.exception;

import com.wl2c.elswhereuserservice.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class HoldingNotFoundException extends LocalizedMessageException {
    public HoldingNotFoundException() { super(HttpStatus.NOT_FOUND, "notfound.holding-product"); }
}
