package org.lemine.emailvalidator.services.implementations;

import lombok.RequiredArgsConstructor;
import org.lemine.emailvalidator.domain.requests.EmailValidationRequest;
import org.lemine.emailvalidator.domain.responses.EmailValidationResponse;
import org.lemine.emailvalidator.services.EmailValidationService;
import org.lemine.emailvalidator.utils.EmailUtils;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailValidationServiceImpl implements EmailValidationService {

    private final EmailUtils emailUtils;

    @Override
    public EmailValidationResponse validateEmail(EmailValidationRequest emailValidationRequest) {
        if(!emailUtils.isSyntaxValid(emailValidationRequest.email()))
            return new EmailValidationResponse(false, "Invalid email syntax");

        else if(!emailUtils.hasValidMXRecord(emailValidationRequest.email()))
            return new EmailValidationResponse(false, "No valid MX record");
        else if(!emailUtils.isSmtpValid(emailValidationRequest.email()))
            return new EmailValidationResponse(false, "Invalid SMTP server");

        return new EmailValidationResponse(true, "Valid email");
    }
}
