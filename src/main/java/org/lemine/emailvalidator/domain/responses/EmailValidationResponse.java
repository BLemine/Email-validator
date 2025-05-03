package org.lemine.emailvalidator.domain.responses;

public record EmailValidationResponse(
        boolean isValid,
        String message
) {
}
