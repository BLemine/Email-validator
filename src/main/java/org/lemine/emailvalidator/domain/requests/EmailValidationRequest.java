package org.lemine.emailvalidator.domain.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailValidationRequest(
        @NotBlank
        @Email
        String email
) {
}
