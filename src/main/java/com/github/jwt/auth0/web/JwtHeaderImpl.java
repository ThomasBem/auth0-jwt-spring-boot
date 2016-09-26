package com.github.jwt.auth0.web;

import com.github.jwt.auth0.config.Auth0JwtConfig;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@EnableEncryptableProperties
public class JwtHeaderImpl implements JwtHeader {

    @Autowired
    private Auth0JwtConfig config;

    @Autowired
    private HttpServletRequest request;

    public Map<String, String> get() {
        try {
            Optional<String> jwt = getJwt();
            if (jwt.isPresent()) {
                return createClaimsMap(jwt.get());
            }
        } catch (IllegalStateException e) {
            log.warn("Unable to get JWT from header, skipping token");
            log.warn(e.getMessage());
        }

        return Collections.emptyMap();
    }

    private Map<String, String> createClaimsMap(String jwt) {
        byte[] key = Base64Utils.decodeFromUrlSafeString(config.getJwtSecret());
        Jws<Claims> claims;
        if (config.isJwtSecretEncoded()) {
            claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);
        } else {
            claims = Jwts.parser().setSigningKey(config.getJwtSecret()).parseClaimsJws(jwt);
        }

        Claims body = claims.getBody();

        Map<String, String> values = new HashMap<>();
        body.keySet().forEach(claimKey -> {
            Object value = body.get(claimKey);
            values.put(claimKey, value.toString());
        });
        return values;
    }

    public Optional<String> getJwt() {
        String jwt = request.getHeader(config.getJwtKey());
        if (StringUtils.isEmpty(jwt)) {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (!StringUtils.isEmpty(authorization)) {
                jwt = authorization.replace("Bearer ", "");
            }
        }
        if (validToken(jwt)) {
            return Optional.ofNullable(jwt);
        } else {
            return Optional.empty();
        }
    }

    private boolean validToken(String jwt) {
        if (StringUtils.isEmpty(jwt)) {
            return false;
        }

        try {
            if (config.isJwtSecretEncoded()) {
                byte[] key = Base64Utils.decodeFromUrlSafeString(config.getJwtSecret());
                Jwts.parser().setSigningKey(key).parse(jwt);
            } else {
                Jwts.parser().setSigningKey(config.getJwtSecret().getBytes()).parse(jwt);
            }
            return true;
        } catch (JwtException e) {
            log.error("Exception when parsing jwt", e);
            throw e;
        }
    }
}
