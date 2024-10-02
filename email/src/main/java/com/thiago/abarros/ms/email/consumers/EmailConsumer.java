package com.thiago.abarros.ms.email.consumers;

import com.thiago.abarros.ms.email.dtos.EmailRecordDTO;
import com.thiago.abarros.ms.email.models.EmailModel;
import com.thiago.abarros.ms.email.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * This class provides a service for consuming email messages from a message queue.
 * It encapsulates the logic for preparing and sending email messages.
 */
@Component
public class EmailConsumer {

    final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Listens to the email queue and processes incoming email messages.
     *
     * @param emailRecordDTO the email record DTO containing the email data
     */
    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenEmailQueue(@Payload EmailRecordDTO emailRecordDTO) {
        var emailModel = new EmailModel();
        BeanUtils.copyProperties(emailRecordDTO, emailModel);
        emailService.sendEmail(emailModel);
    }
}
