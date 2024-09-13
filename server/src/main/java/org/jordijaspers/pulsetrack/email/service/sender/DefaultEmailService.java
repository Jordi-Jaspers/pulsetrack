package org.jordijaspers.pulsetrack.email.service.sender;

import lombok.RequiredArgsConstructor;
import org.hawaiiframework.exception.HawaiiException;
import org.jordijaspers.pulsetrack.email.model.MailMessage;
import org.jordijaspers.pulsetrack.email.service.message.MailMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

/**
 * Service (infrastructure) to send emails with.
 */
@Service
@RequiredArgsConstructor
@Profile("!development")
public class DefaultEmailService implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    private final MailMessageFactory mailMessageFactory;

    @Override
    public void sendEmail(final String recipient, final MailMessage mailMessage) {
        final MimeMessagePreparator messagePreparation = mimeMessage -> {
            final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(recipient);
            messageHelper.setFrom(mailMessage.getFrom());
            messageHelper.setSubject(mailMessage.getSubject());
            messageHelper.setText(mailMessage.getBody(), mailMessage.isHtml());
        };
        try {
            LOGGER.debug("Sending email to '{}' with subject '{}'.", recipient, mailMessage.getSubject());
            mailSender.send(messagePreparation);
        } catch (MailException e) {
            throw new HawaiiException("Error sending email.", e);
        }
    }
}
