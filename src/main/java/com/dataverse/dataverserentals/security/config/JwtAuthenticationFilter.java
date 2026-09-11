package com.dataverse.dataverserentals.security.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;
import com.dataverse.dataverserentals.security.services.JwtService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JwtConfig jwtConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith(jwtConfig.getTipo() + " ")) {
            log.warn("auth header does not include a JWT token");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(jwtConfig.getTipo().length() + 1);

        try {
            UsuarioPrincipal userPrincipal = jwtService.getUsuarioPrincipalFromToken(token);
            log.info("usuario de token [usuarioId={}] [usuarioNombre={}] [scope={}] [usuarioRol={}]",
                    userPrincipal.usuarioId(),
                    userPrincipal.usuarioNombre(),
                    userPrincipal.scope(),
                    userPrincipal.usuarioRol());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userPrincipal,
                    null,
                    this.getAuthorities(userPrincipal));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("usuario autenticado [usuarioId={}] [usuarioNombre={}] [scope={}] [usuarioRol={}]",
                    userPrincipal.usuarioId(),
                    userPrincipal.usuarioNombre(),
                    userPrincipal.scope(),
                    userPrincipal.usuarioRol());

        } catch (JwtException e) {
            log.warn("Token JWT inválido: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private List<GrantedAuthority> getAuthorities(UsuarioPrincipal userPrincipal) {
        return List.of(new SimpleGrantedAuthority(userPrincipal.scope().asAuthority()));
    }
}