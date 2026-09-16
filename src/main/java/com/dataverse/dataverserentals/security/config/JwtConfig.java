package com.dataverse.dataverserentals.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "dataverse.jwt")
public class JwtConfig {
    private String secret;
    private long expiration;
    private String tipo = "Bearer";
    private String cookieName = "jwt_token";
    private boolean cookieSecure = false;
}