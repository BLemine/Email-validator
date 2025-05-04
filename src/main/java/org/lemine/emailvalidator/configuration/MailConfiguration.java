package org.lemine.emailvalidator.configuration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "validator.mail")
@Getter
@Setter
@Validated
public class MailConfiguration {
    @Positive
    String port;
    @NotBlank
    String domain;
    @Email
    String source;
    @PositiveOrZero
    Integer timeout;
    @PositiveOrZero
    Integer connectionTimeout;
}
