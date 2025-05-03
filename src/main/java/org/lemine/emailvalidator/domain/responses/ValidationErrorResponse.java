package org.lemine.emailvalidator.domain.responses;

import java.util.Map;

public record ValidationErrorResponse(
        String code,
        String message,
        Map<String, String> errors
) {
}
