package org.lemine.emailvalidator.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lemine.emailvalidator.domain.requests.EmailValidationRequest;
import org.lemine.emailvalidator.services.EmailValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email-validator")
@RequiredArgsConstructor
public class EmailValidationController {

    private final EmailValidationService emailValidationService;

    @PostMapping
    public ResponseEntity<?> validateEmail(@Valid EmailValidationRequest emailValidationRequest) {
        return new ResponseEntity<>(
                emailValidationService.validateEmail(emailValidationRequest),
                HttpStatus.OK
        );
    }
}
