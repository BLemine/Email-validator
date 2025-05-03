package org.lemine.emailvalidator.services.implementations;

import org.lemine.emailvalidator.domain.requests.EmailValidationRequest;
import org.lemine.emailvalidator.domain.responses.EmailValidationResponse;
import org.lemine.emailvalidator.services.EmailValidationService;
import org.lemine.emailvalidator.utils.EmailUtils;
import org.springframework.stereotype.Service;


@Service
public class EmailValidationServiceImpl implements EmailValidationService {

    @Override
    public EmailValidationResponse validateEmail(EmailValidationRequest emailValidationRequest) {
        if(!EmailUtils.isSyntaxValid(emailValidationRequest.email()))
            return new EmailValidationResponse(false, "Invalid email syntax");

        else if(!EmailUtils.hasValidMXRecord(emailValidationRequest.email()))
            return new EmailValidationResponse(false, "No valid MX record");
        else if(!EmailUtils.isSmtpValid(emailValidationRequest.email()))
            return new EmailValidationResponse(false, "Invalid SMTP server");

        return new EmailValidationResponse(true, "Valid email");
    }
}
