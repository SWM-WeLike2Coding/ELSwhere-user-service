package com.wl2c.elswhereuserservice.domain.user.exception;

import com.wl2c.elswhereuserservice.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class NotOAuthAuthorizedException extends LocalizedMessageException {
    public NotOAuthAuthorizedException() {
        super(HttpStatus.BAD_REQUEST, "required.oauth-authorization");
    }
}
