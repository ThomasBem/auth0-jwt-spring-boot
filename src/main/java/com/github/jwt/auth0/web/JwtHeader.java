package com.github.jwt.auth0.web;

import java.util.Map;
import java.util.Optional;

public interface JwtHeader {
    Map<String, String> get();
    Optional<String> getJwt();
}
