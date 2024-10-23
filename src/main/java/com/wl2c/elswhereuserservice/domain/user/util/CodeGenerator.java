package com.wl2c.elswhereuserservice.domain.user.util;

import java.util.Random;
import java.util.UUID;

public class CodeGenerator {

    public static String generateUUIDCode() {
        return UUID.randomUUID().toString();
    }

}
