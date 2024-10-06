package com.thiago.abarros.ms.email.consumers;

import com.thiago.abarros.ms.email.dtos.EmailRecordDTO;
import com.thiago.abarros.ms.email.dtos.RecoverPasswordDTO;
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

    @RabbitListener(queues = "${broker.queue.recover.password}")
    public void listenRecoverPasswordQueue(@Payload RecoverPasswordDTO emailRecordDTO) {
        var emailModel = new EmailModel();
        BeanUtils.copyProperties(emailRecordDTO, emailModel);
        emailService.sendEmail(emailModel);
    }

    @RabbitListener(queues = "${broker.queue.register}")
    public void listenRegisterQueue(@Payload EmailRecordDTO emailRecordDTO) {
        var emailModel = new EmailModel();
        BeanUtils.copyProperties(emailRecordDTO, emailModel);
        emailService.sendEmail(emailModel);
    }
}
