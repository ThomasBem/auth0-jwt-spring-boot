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
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Optional<String> getName() {
        Optional<String> nickname = getProperty("nickname");
        if (nickname.isPresent()) {
            String[] names = nickname.get().split("\\.");
            String name = Arrays.stream(names).map(StringUtils::capitalize).collect(Collectors.joining(" "));
            return Optional.of(name);
        }
        return Optional.empty();
    }

    public <T> T get(Class<T> type) {
        Map<String, String> jwtData = getJwtData();
        String metadata = jwtData.get(APP_METADATA);
        if (metadata == null || "".equals(metadata)) {
            throw new IllegalArgumentException("Could not find app_metadata in jwt claims");
        } else {
            try {
                return mapToObject(metadata, type);
            } catch (PathNotFoundException | IOException e) {
                log.warn(e.getMessage());
                return null;
            }
        }
    }

    private Map<String, String> getJwtData() {
        Map<String, String> claims = new HashMap<>();
        Optional<String> jwt = jwtHeader.getJwt();
        if (jwt.isPresent()) {
            Jws<Claims> jwtClaims = Jwts.parser().setSigningKey(config.getSecret()).parseClaimsJws(jwt.get());
            Claims body = jwtClaims.getBody();
            body.keySet().forEach(claimKey -> {
                Object value = body.get(claimKey);
                claims.put(claimKey, value.toString());
            });
        }

        return claims;
    }

    @SuppressWarnings("unchecked")
    private <T> T mapToObject(String metadata, Class<T> type) throws IOException {
        String json = JsonPath.parse(metadata.replaceAll("=", ":")).jsonString();
        return objectMapper.readValue(json, type);
    }
}