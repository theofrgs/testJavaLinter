package com.outside;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;

@org.springframework.stereotype.Service
public class JWTService extends Service {

    public JWTService() {
        this.initLogger(JWTService.class);
    }

    public String createJWTToken(Map<String, Object> payloadClaims) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(Objects.requireNonNull(properties.getProperty("jwt.secret")));
            return JWT.create()
                    .withClaim("data", payloadClaims)
                    .withIssuer("auth0")
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            this.LOGGER.error(ExceptionUtils.getMessage(e));
            return null;
        }
    }

    public boolean checkToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(Objects.requireNonNull(properties.getProperty("jwt.secret")));
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            this.LOGGER.error(ExceptionUtils.getMessage(e));
            return false;
        }
    }

    public Map<String, Claim> getTokenContent(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(Objects.requireNonNull(properties.getProperty("jwt.secret")));
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaims();
        } catch (JWTVerificationException e) {
            this.LOGGER.error(ExceptionUtils.getMessage(e));
            return null;
        }
    }
}