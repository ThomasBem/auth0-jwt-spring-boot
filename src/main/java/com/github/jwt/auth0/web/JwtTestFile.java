package com.github.jwt.auth0.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Component
public class JwtTestFile {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${jwt-test-token-file:test-jwt.json}")
    private String tokenFile;

    @SuppressWarnings("unchecked")
    public Map<String, String> getClaims() {
        try {
            URL resource = JwtTestFile.class.getResource("/" + tokenFile);
            if (resource == null) {
                return Collections.emptyMap();
            }

            byte[] content = Files.readAllBytes(Paths.get(resource.toURI()));
            return objectMapper.readValue(content, Map.class);
        } catch (IOException | URISyntaxException e) {
            log.info(String.format("Unable to read file %s", tokenFile), e);
            return Collections.emptyMap();
        }
    }
}
