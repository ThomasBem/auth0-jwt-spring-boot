package com.github.jwt.auth0.web;

import com.github.jwt.auth0.config.Auth0JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JwtHeaderMock implements JwtHeader {

    @Value("${jwt-test-token:}")
    private String jwtTestToken;

    @Autowired
    private Auth0JwtConfig auth0JwtConfig;

    @Autowired
    private JwtTestFile jwtTestFile;

    private Map<String, String> claims = new HashMap<>();

    @PostConstruct
    public void init() {
        Map<String, String> fileClaims = jwtTestFile.getClaims();
        if (fileClaims == null || fileClaims.isEmpty()) {
            claims.put("user_id", "123");
            claims.put("name", "john.doe@test.com");
            claims.put("nickname", "john.doe");
            claims.put("email", "john.doe@test.com");
            claims.put("email_verified", "true");
            claims.put("picture", "https://s.gravatar.com/avatar");
        } else {
            claims = fileClaims;
        }
    }

    @Override
    public Map<String, String> get() {
        if (!StringUtils.isEmpty(jwtTestToken)) {
            claims.clear();

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(auth0JwtConfig.getSecret()).parseClaimsJws(jwtTestToken);
            Claims claimsBody = claimsJws.getBody();
            claimsBody.entrySet().forEach(claim -> claims.put(claim.getKey(), String.valueOf(claim.getValue())));
        }

        return claims;
    }

    @Override
    public Optional<String> getJwt() {
        if (StringUtils.isEmpty(jwtTestToken)) {
            Map<String, Object> claimMap = new HashMap<>(claims);
            String jwt = Jwts.builder().setClaims(claimMap).signWith(SignatureAlgorithm.HS256, auth0JwtConfig.getSecret()).compact();
            return Optional.of(jwt);
        } else {
            return Optional.of(jwtTestToken);
        }
    }
}
