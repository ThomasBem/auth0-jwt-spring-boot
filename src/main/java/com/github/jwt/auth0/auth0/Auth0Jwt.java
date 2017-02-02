package com.github.jwt.auth0.auth0;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jwt.auth0.config.Auth0JwtConfig;
import com.github.jwt.auth0.web.JwtHeader;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class Auth0Jwt {

    private static final String APP_METADATA = "app_metadata";

    @Autowired
    private Auth0JwtConfig config;

    @Autowired
    private JwtHeader jwtHeader;

    @Autowired
    private ObjectMapper objectMapper;

    public Optional<String> getProperty(String key) {
        Map<String, String> jwtData = getJwtData();
        return Optional.ofNullable(jwtData.get(key));
    }

    public <T> T get(String jsonPath, Class<T> type) {
        Map<String, String> jwtData = getJwtData();
        String metadata = jwtData.get(APP_METADATA);
        if (metadata == null || "".equals(metadata)) {
            throw new IllegalArgumentException("Could not find app_metadata in jwt claims");
        } else {
            try {
                return mapToObject(jsonPath, metadata, type);
            } catch(PathNotFoundException e) {
                log.warn(e.getMessage());
                return null;
            }
        }
    }

    private Map<String, String> getJwtData() {
        Map<String, String> claims = new HashMap<>();
        Optional<String> jwt = jwtHeader.getJwt();
        if (jwt.isPresent()) {
            byte[] key = config.getSecret();
            Jws<Claims> jwtClaims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt.get());
            Claims body = jwtClaims.getBody();
            body.keySet().forEach(claimKey -> {
                Object value = body.get(claimKey);
                claims.put(claimKey, value.toString());
            });
        }

        return claims;
    }

    @SuppressWarnings("unchecked")
    private <T> T mapToObject(String jsonPath, String metadata, Class<T> type) {
        String json = JsonPath.parse(metadata.replaceAll("=", ":")).jsonString();
        List<Map<String, Object>> values = JsonPath.parse(json).read(jsonPath, List.class);
        return objectMapper.convertValue(values, type);
    }
}