package org.lemine.emailvalidator.services;

import org.lemine.emailvalidator.domain.requests.EmailValidationRequest;
import org.lemine.emailvalidator.domain.responses.EmailValidationResponse;

public interface EmailValidationService {
    EmailValidationResponse validateEmail(EmailValidationRequest emailValidationRequest);
}
