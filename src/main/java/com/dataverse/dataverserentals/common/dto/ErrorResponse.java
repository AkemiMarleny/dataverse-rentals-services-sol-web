package com.dataverse.dataverserentals.common.dto;

import java.util.ArrayList;
import java.util.List;

public record ErrorResponse(
        String message,
        List<FieldError> errors) {

    public ErrorResponse(String message) {
        this(message, new ArrayList<>());
    }
}
