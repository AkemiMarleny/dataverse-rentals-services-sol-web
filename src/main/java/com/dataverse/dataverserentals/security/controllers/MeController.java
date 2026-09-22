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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/me")
@Slf4j
public class MeController {

    private final AuthenticationService authenticationService;
    private final SecurityContext securityContext;

    @GetMapping()
    public ResponseEntity<CurrentUserResponse> getCurrentUser() throws RecordNotFoundException {
        CurrentUserResponse response = authenticationService.getCurrentUser();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("update-password")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordRequest request)
            throws RecordNotFoundException, UnprocesableEntity {

        authenticationService.updatePassword(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
