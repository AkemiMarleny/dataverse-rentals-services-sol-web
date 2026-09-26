package com.dataverse.dataverserentals.security.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;
import com.dataverse.dataverserentals.security.context.SecurityContext;
import com.dataverse.dataverserentals.security.controllers.dtos.request.UpdatePasswordRequest;
import com.dataverse.dataverserentals.security.controllers.dtos.response.CurrentUserResponse;
import com.dataverse.dataverserentals.security.services.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/me")
@Slf4j
@Tag(name = "Perfil de usuario", description = "Información del usuario autenticado y cambio de contraseña")
public class MeController {

    private final AuthenticationService authenticationService;
    private final SecurityContext securityContext;

    @GetMapping()
    @Operation(
            summary = "Obtener usuario autenticado",
            description = "Devuelve los datos del usuario que realizó la petición, según el token JWT.")
    @ApiResponse(responseCode = "200", description = "Datos del usuario autenticado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<CurrentUserResponse> getCurrentUser() throws RecordNotFoundException {
        CurrentUserResponse response = authenticationService.getCurrentUser();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("update-password")
    @Operation(
            summary = "Cambiar contraseña",
            description = "Actualiza la contraseña del usuario autenticado, validando la contraseña anterior.")
    @ApiResponse(responseCode = "204", description = "Contraseña actualizada correctamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "422", description = "La contraseña anterior es incorrecta o la nueva no coincide con la confirmación")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordRequest request)
            throws RecordNotFoundException, UnprocesableEntity {

        authenticationService.updatePassword(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}