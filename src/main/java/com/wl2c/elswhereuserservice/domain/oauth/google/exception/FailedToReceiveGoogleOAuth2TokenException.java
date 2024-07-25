package com.wl2c.elswhereuserservice.domain.oauth.google.exception;

import com.wl2c.elswhereuserservice.global.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class FailedToReceiveGoogleOAuth2TokenException extends LocalizedMessageException {
    public FailedToReceiveGoogleOAuth2TokenException() { super(HttpStatus.INTERNAL_SERVER_ERROR, "failed.get-google-oauth-token"); }
}
