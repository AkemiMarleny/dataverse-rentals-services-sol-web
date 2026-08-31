package com.dataverse.dataverserentals.security.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
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

    @GetMapping()
    public ResponseEntity<CurrentUserResponse> getCurrentUser() throws RecordNotFoundException {
        CurrentUserResponse response = authenticationService.getCurrentUser();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
