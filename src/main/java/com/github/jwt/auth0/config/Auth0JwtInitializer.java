package com.github.jwt.auth0.config;

import com.github.jwt.auth0.auth0.Auth0Metadata;
import com.github.jwt.auth0.web.JwtHeader;
import com.github.jwt.auth0.web.JwtHeaderImpl;
import com.github.jwt.auth0.web.JwtHeaderMock;
import com.github.jwt.auth0.web.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Auth0JwtInitializer {

    @Autowired
    private Auth0JwtConfig auth0JwtConfig;

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
        if (auth0JwtConfig.isJwtTestMode()) {
            return new JwtHeaderMock();
        } else {
            return new JwtHeaderImpl();
        }
    }
}
