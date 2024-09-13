package org.jordijaspers.pulsetrack.email.service.sender;

import org.jordijaspers.pulsetrack.email.model.MailMessage;

/**
 * An interface for sending emails.
 */
public interface EmailService {

    /**
     * Send an email as defined in {@code mailMessage}.
     */
    void sendEmail(String recipient, MailMessage mailMessage);
}
