package com.wl2c.elswhereuserservice.domain.user.exception;

import com.wl2c.elswhereuserservice.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class RequiredTermsAgreementException extends LocalizedMessageException {
    public RequiredTermsAgreementException() {
        super(HttpStatus.FORBIDDEN, "required.terms-agreement");
    }
}
