package com.thiago.abarros.ms.user.producers;

import com.thiago.abarros.ms.user.dtos.EmailDTO;
import com.thiago.abarros.ms.user.models.UserModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {
  final RabbitTemplate rabbitTemplate;

  public UserProducer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Value(value = "${broker.queue.email.name}")
  private String routingKey;

  public void publishMessageEmail(UserModel userModel) {
    var emailDTO = new EmailDTO(
        userModel.getUserId(),
        userModel.getEmail(),
        "Cadastro realizado com sucesso!",
        userModel.getName() + ", seu cadastro foi realizado com sucesso!");
    rabbitTemplate.convertAndSend("", routingKey, emailDTO);
  }
}
