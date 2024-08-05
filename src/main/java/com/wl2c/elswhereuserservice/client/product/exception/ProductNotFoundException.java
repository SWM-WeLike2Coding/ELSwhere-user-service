package com.wl2c.elswhereuserservice.client.product.exception;

import com.wl2c.elswhereuserservice.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends LocalizedMessageException {
    public ProductNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.product");
    }
    public ProductNotFoundException(Exception e) {
        super(e, HttpStatus.NOT_FOUND, "notfound.product");
    }
}