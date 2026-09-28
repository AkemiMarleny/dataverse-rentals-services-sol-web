package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request;

import java.time.LocalDate;

import com.dataverse.dataverserentals.common.annotations.ValidDocumentFormat;
import com.dataverse.dataverserentals.gestionadministrativa.enums.Genero;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoPersona;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreatePersonDto(
        @NotNull(message = "El tipo de persona es obligatorio")
        @Schema(description = "Tipo de persona (NATURAL_CON_NEGOCIO, NATURAL_SIN_NEGOCIO, JURIDICA)", example = "NATURAL_SIN_NEGOCIO", requiredMode = Schema.RequiredMode.REQUIRED)
        TipoPersona tipoPersonaCode,

        // Campos comunes
        @NotNull
        @Schema(description = "Tipo de documento (DNI, RUC, CARNET_EXTRANJERIA)", example = "DNI", requiredMode = Schema.RequiredMode.REQUIRED)
        TipoDocumento tipoDocumentoCode,
        @NotBlank(message = "El número de documento es obligatorio") @ValidDocumentFormat
        @Schema(description = "Número de documento", example = "12345678", requiredMode = Schema.RequiredMode.REQUIRED)
        String numeroDocumento,
        @NotBlank @Pattern(regexp = "^\\(\\+51\\) [0-9]{9}$", message = "Formato de teléfono incorrecto")
        @Schema(description = "Número de celular (formato (+51) 999888777)", example = "(+51) 999888777", requiredMode = Schema.RequiredMode.REQUIRED)
        String numeroCelular,
        @NotBlank @Email
        @Schema(description = "Correo electrónico", example = "juan.perez@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String correo,

        // Campos para persona natural
        @Schema(description = "Nombres (obligatorio para persona natural)", example = "Juan Carlos")
        String nombres,
        @Schema(description = "Apellidos (obligatorio para persona natural)", example = "Pérez Gómez")
        String apellidos,
        @Schema(description = "Género (FEMENINO, MASCULINO)", example = "MASCULINO")
        Genero generoCode,
        @Schema(description = "Fecha de nacimiento", example = "1990-05-15")
        LocalDate fechaNacimiento,

        // Campo para persona natural con negocio
        @Schema(description = "Nombre comercial (obligatorio para persona natural con negocio)", example = "Bodega Pérez")
        String nombreComercial,

        // Campos para persona jurídica
        @Schema(description = "Razón social (obligatorio para persona jurídica)", example = "Pérez & Hijos S.A.C.")
        String razonSocial) {

}