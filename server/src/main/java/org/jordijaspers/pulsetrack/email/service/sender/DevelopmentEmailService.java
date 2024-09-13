package org.jordijaspers.pulsetrack.email.service.sender;

import lombok.RequiredArgsConstructor;
import org.jordijaspers.pulsetrack.email.model.MailMessage;
import org.jordijaspers.pulsetrack.email.service.message.MailMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * A service to send emails in a development environment.
 */
@Service
@Profile("development")
@RequiredArgsConstructor
public class DevelopmentEmailService implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final MailMessageFactory mailMessageFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendEmail(final String recipient, final MailMessage mailMessage) {
        LOGGER.info("Sending email to user '{}' with content: \n{}", recipient, mailMessage);
    }
}
