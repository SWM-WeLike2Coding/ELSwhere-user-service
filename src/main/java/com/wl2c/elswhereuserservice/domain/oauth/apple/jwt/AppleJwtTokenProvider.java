package com.wl2c.elswhereuserservice.domain.oauth.apple.jwt;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Service
public class AppleJwtTokenProvider {

    public String createJwtToken(String clientId, String teamId, String keyId, String privateKeyPath) throws Exception {
        // Private key를 가져옴
        String privateKeyPem;
        try (InputStream inputStream = getClass().getResourceAsStream(privateKeyPath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Private key file not found: " + privateKeyPath);
            }
            privateKeyPem = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        // PEM에서 필요없는거 제거하고 base64로 디코드
        String privateKeyBase64 = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(privateKeyBase64);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PrivateKey privateKey = keyFactory.generatePrivate(spec);

        // JWT 헤더 생성
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                .keyID(keyId) // key identifier
                .build();

        // JWT Claim 생성
        Instant now = Instant.now();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer(teamId) // 팀 ID
                .issueTime(Date.from(now)) // 발행 일자
                .expirationTime(Date.from(now.plusSeconds(15777000))) // 6개월 유효
                .audience("https://appleid.apple.com") // Audience
                .subject(clientId) // App ID 혹은 Service ID
                .build();

        // JWT에 사인
        JWSSigner signer = new ECDSASigner((ECPrivateKey) privateKey);
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        signedJWT.sign(signer);

        // 직렬화한 토큰 반환
        return signedJWT.serialize();
    }
}
