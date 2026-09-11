package com.dataverse.dataverserentals.security.controllers.dtos.response;

public record LoginResponse(
        String token,
        String tipo,
        String status) {
}
