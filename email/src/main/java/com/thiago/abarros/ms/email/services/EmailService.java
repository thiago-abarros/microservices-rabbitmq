package com.thiago.abarros.ms.email.services;

import com.thiago.abarros.ms.email.enums.StatusEmail;
import com.thiago.abarros.ms.email.models.EmailModel;
import com.thiago.abarros.ms.email.repositories.EmailRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


/**
 * This class provides a service for sending emails.
 * It encapsulates the logic for preparing and sending email messages.
 */
@Service
public class EmailService {

    final EmailRepository emailRepository;
    final JavaMailSender emailSender;

    /**
     * The email address used as the sender for email messages.
     */
    @Value(value = "${spring.mail.username}")
    private String emailFrom;

    public EmailService(EmailRepository emailRepository, JavaMailSender emailSender) {
        this.emailRepository = emailRepository;
        this.emailSender = emailSender;
    }

    /**
     * Sends an email with the given email model.
     * This method prepares the email message, sends it, and updates the email model with the result.
     *
     * @param emailModel the email model containing the email data
     * @return the updated email model with the result of the send operation
     */
    @Transactional
    public EmailModel sendEmail(EmailModel emailModel) {
        try {
            emailModel.setSendDateEmail(LocalDateTime.now());
            emailModel.setEmailFrom(emailFrom);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());

            emailSender.send(message);

            emailModel.setStatusEmail(StatusEmail.SENT);
        } catch (MailException e) {
            emailModel.setStatusEmail(StatusEmail.ERROR);
        } finally {
            return emailRepository.save(emailModel);
        }
    }
}
