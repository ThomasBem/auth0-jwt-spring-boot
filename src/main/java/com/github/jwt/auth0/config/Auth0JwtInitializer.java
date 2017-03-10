package com.github.jwt.auth0.config;

import com.github.jwt.auth0.auth0.Auth0Jwt;
import com.github.jwt.auth0.web.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class Auth0JwtInitializer {

    @Value("${jwt-test-mode:false}")
    private String jwtTestMode;

    @Bean
    public Auth0Jwt auth0Jwt() {
        return new Auth0Jwt();
    }

    @Bean
    public Auth0JwtConfig auth0JwtConfig() {
        return new Auth0JwtConfig();
    }

    @Bean
    public RequestUtil requestUtil() {
        return new RequestUtil();
    }

    @Bean
    public JwtTestFile jwtTokenFile() {
        return new JwtTestFile();
    }

    @Bean
    public JwtHeader jwtHeader() {
        Boolean isJwtTestMode = Boolean.valueOf(jwtTestMode);
        if (isJwtTestMode) {
            log.info("JwtHeader started in test mode");
            return new JwtHeaderMock();
        } else {
            return new JwtHeaderImpl();
        }
    }
}
