package com.wl2c.elswhereuserservice.global.exception;

import org.springframework.http.HttpStatus;

public class FailedOAuthCallbackProcessingException extends LocalizedMessageException {
    public FailedOAuthCallbackProcessingException(Exception e) { super(e, HttpStatus.INTERNAL_SERVER_ERROR, "failed.oauth-callback-processing"); }
}
