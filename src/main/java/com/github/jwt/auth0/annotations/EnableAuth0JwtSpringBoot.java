package com.github.jwt.auth0.annotations;

import com.github.jwt.auth0.config.Auth0JwtConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(Auth0JwtConfig.class)
public @interface EnableAuth0JwtSpringBoot {
}
