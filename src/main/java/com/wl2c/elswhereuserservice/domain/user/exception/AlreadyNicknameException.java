package com.wl2c.elswhereuserservice.domain.user.exception;

import com.wl2c.elswhereuserservice.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class AlreadyNicknameException extends LocalizedMessageException {
    public AlreadyNicknameException() {
        super(HttpStatus.BAD_REQUEST, "already.nickname");
    }
}
