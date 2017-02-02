package com.github.jwt.auth0.web;

import com.github.jwt.auth0.config.Auth0JwtConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RequestUtil {

    @Autowired
    private Auth0JwtConfig config;

    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    @Autowired
    private JwtHeader jwtHeader;

    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, Class<T> responseType) {
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(url, method, createRequestEntity(), responseType);
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        } catch (HttpClientErrorException e) {
            log.warn("Http client error, url:{} status-code:{} message:{}", url, e.getStatusCode(), e.getMessage());
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    HttpEntity createRequestEntity() {
        HttpHeaders header = createAuthorizationHeader();
        jwtHeader.getJwt().ifPresent(jwtToken -> header.add(config.getJwtKey(), jwtToken));
        return new HttpEntity<>(null, header);
    }

    private HttpHeaders createAuthorizationHeader() {
        HttpHeaders header = new HttpHeaders();
        if (!StringUtils.isEmpty(config.getUsername()) && !StringUtils.isEmpty(config.getPassword())) {
            String authString = config.getUsername() + ":" + config.getPassword();
            String encoded = Base64.encodeBase64String(authString.getBytes());
            String authorization = "Basic " + encoded;
            header.set("Authorization", authorization);
        }
        return header;
    }


}
