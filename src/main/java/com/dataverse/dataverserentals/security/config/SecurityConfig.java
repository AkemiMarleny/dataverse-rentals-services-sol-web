package com.dataverse.dataverserentals.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dataverse.dataverserentals.security.enums.TokenScope;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/login", "/api/v1/auth/logout").permitAll()
                        .requestMatchers("/api/v1/auth/mfa/setup", "/api/v1/auth/mfa/enable")
                        .hasAnyAuthority(TokenScope.MFA_SETUP.asAuthority(), TokenScope.FULL_ACCESS.asAuthority())
                        .requestMatchers("/api/v1/auth/mfa/verify").hasAuthority(TokenScope.MFA_VERIFY.asAuthority())
                        .requestMatchers("/api/v1/me").hasAnyAuthority(
                                TokenScope.MFA_VERIFY.asAuthority(),
                                TokenScope.MFA_SETUP.asAuthority(),
                                TokenScope.FULL_ACCESS.asAuthority())
                        .anyRequest().hasAuthority(TokenScope.FULL_ACCESS.asAuthority()))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}