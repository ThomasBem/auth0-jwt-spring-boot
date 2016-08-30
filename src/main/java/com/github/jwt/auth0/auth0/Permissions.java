package com.github.jwt.auth0.auth0;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Permissions {
    private List<Permission> permissions;
}
