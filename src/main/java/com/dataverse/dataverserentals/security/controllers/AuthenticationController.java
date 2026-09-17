package com.dataverse.dataverserentals.security.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.security.controllers.dtos.request.LoginRequest;
import com.dataverse.dataverserentals.security.controllers.dtos.response.LoginResponse;
import com.dataverse.dataverserentals.security.exceptions.CredencialesInvalidasException;
import com.dataverse.dataverserentals.security.exceptions.UsuarioBloqueadoException;
import com.dataverse.dataverserentals.security.exceptions.UsuarioInactivoException;
import com.dataverse.dataverserentals.security.services.AuthenticationService;
import com.dataverse.dataverserentals.security.services.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse httpResponse)
            throws CredencialesInvalidasException, UsuarioInactivoException, UsuarioBloqueadoException {
        log.info("Intento de autenticación para usuario: {}", request.nombreUsuario());
        LoginResponse loginResponse = authenticationService.authenticate(request);

        if (loginResponse.status().equals("SUCCESS")) {
            Cookie jwtCookie = jwtService.toCookie(loginResponse.token());
            httpResponse.addCookie(jwtCookie);
        }

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        log.info("Cerrando sesión de usuario");
        Cookie jwtCookie = jwtService.toLogoutCookie();
        httpResponse.addCookie(jwtCookie);
        return ResponseEntity.ok().build();
    }

}