package com.wl2c.elswhereuserservice.domain.oauth.apple.exception;

import com.wl2c.elswhereuserservice.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class FailedToReceiveAppleOAuth2TokenException extends LocalizedMessageException {
    public FailedToReceiveAppleOAuth2TokenException() { super(HttpStatus.INTERNAL_SERVER_ERROR, "failed.get-apple-oauth-token"); }
}
