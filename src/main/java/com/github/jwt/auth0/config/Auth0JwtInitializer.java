package com.github.jwt.auth0.config;

import com.github.jwt.auth0.auth0.Auth0Metadata;
import com.github.jwt.auth0.web.JwtHeader;
import com.github.jwt.auth0.web.JwtHeaderImpl;
import com.github.jwt.auth0.web.JwtHeaderMock;
import com.github.jwt.auth0.web.RequestUtil;
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
    public Auth0Metadata auth0Metadata() {
        return new Auth0Metadata();
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
