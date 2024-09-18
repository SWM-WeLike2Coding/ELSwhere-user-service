package com.wl2c.elswhereuserservice.domain.user.exception;

import com.wl2c.elswhereuserservice.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class ProductLikeNotFoundException extends LocalizedMessageException {
    public ProductLikeNotFoundException() { super(HttpStatus.NOT_FOUND, "notfound.product-like"); }
}
