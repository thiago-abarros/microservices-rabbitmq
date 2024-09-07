package com.thiago.abarros.ms.email.consumers;

import com.thiago.abarros.ms.email.dtos.EmailRecordDTO;
import com.thiago.abarros.ms.email.models.EmailModel;
import com.thiago.abarros.ms.email.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {
  final EmailService emailService;

  public EmailConsumer(EmailService emailService) {
    this.emailService = emailService;
  }

  @RabbitListener(queues = "${broker.queue.email.name}")
  public void listenEmailQueue(@Payload EmailRecordDTO emailRecordDTO) {
    var emailModel = new EmailModel();
    BeanUtils.copyProperties(emailRecordDTO, emailModel);
    emailService.sendEmail(emailModel);
  }
}
