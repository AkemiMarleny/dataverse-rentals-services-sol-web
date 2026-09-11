package com.dataverse.dataverserentals.common.config;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dataverse.dataverserentals.common.dto.ErrorResponse;
import com.dataverse.dataverserentals.common.dto.FieldError;
import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.exceptions.Unauthorized;
import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ControllerExceptionsAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generic(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Unauthorized.class)
    public ResponseEntity<ErrorResponse> unauthorized(Unauthorized e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> sqlException(DataIntegrityViolationException e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse(
                "Esta operación no se puede realizar porque otros registros en la base de datos dependen de la información que está intentando modificar o eliminar."),
                HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> conflicts(ConflictException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(RecordNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FieldConstraintException.class)
    public ResponseEntity<ErrorResponse> fieldConstraint(FieldConstraintException e) {
        return new ResponseEntity<>(
                new ErrorResponse("BAD REQUEST", Arrays.asList(new FieldError(e.getField(), e.getMessage()))),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnprocesableEntity.class)
    public ResponseEntity<ErrorResponse> unprocesableEntity(UnprocesableEntity e) {
        return new ResponseEntity<>(new ErrorResponse(
                e.getMessage()), HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> constraints(ConstraintViolationException e) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        "BAD REQUEST",
                        buildFieldErrors(e.getConstraintViolations())),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> conflicts(ObjectOptimisticLockingFailureException e) {
        return new ResponseEntity<>(new ErrorResponse(
                "Conflicto de actualización: el objeto que está intentando modificar ha sido actualizado por otro proceso. Por favor, recargue los datos e intente nuevamente."),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDeniedException(AccessDeniedException e) {
        log.warn("[FORBIDDEN] : {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse("Usuario no autorizado"), HttpStatus.FORBIDDEN);
    }

    private List<FieldError> buildFieldErrors(Set<ConstraintViolation<?>> constraintViolations) {
        return constraintViolations.stream().map(constraint -> {
            String field = StreamSupport.stream(constraint.getPropertyPath().spliterator(), false)
                    .reduce((_, second) -> second)
                    .orElse(null)
                    .toString();

            return new FieldError(field, constraint.getMessage());
        }).collect(Collectors.toList());
    }
}
