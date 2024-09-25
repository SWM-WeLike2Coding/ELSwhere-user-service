package com.wl2c.elswhereuserservice.global.auth.jwt;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;

import java.text.ParseException;
import java.util.Map;

public class JwtDecoder {
    static public Map<String, Object> decode(String token) throws ParseException {
        JWT jwt = JWTParser.parse(token);
        return jwt.getJWTClaimsSet().getClaims();
    }
}
