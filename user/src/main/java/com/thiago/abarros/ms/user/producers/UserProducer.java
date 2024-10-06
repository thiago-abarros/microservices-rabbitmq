package com.thiago.abarros.ms.user.producers;

import com.thiago.abarros.ms.user.dtos.EmailDTO;
import com.thiago.abarros.ms.user.dtos.RecoverPasswordDTO;
import com.thiago.abarros.ms.user.models.User;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Produces messages for user registration and password recovery.
 *
 * @see #publishRegisterMessageEmail(User)
 * @see #publishRecoverPasswordMessageEmail(User, String)
 */
@Component
public class UserProducer {

    final RabbitTemplate rabbitTemplate;
    /**
     * The routing key for the register email queue.
     */
    @Value(value = "${broker.queue.register}")
    private String routingKeyRegister;

    /**
     * The routing key for the recover password queue.
     */
    @Value(value = "${broker.queue.recover.password}")
    private String routingKeyRecoverPassword;

    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Publishes a registration message to the email queue.
     *
     * @param user the user to send the registration message to
     */
    public void publishRegisterMessageEmail(User user) {

        final int priority = 2;

        var emailDTO = new EmailDTO(
                user.getUserId(),
                user.getEmail(),
                "Cadastro de Usuário",
                "Seja bem-vindo(a) " + user.getName() + ", seu cadastro foi realizado com sucesso!"
        );
        rabbitTemplate.convertAndSend("", routingKeyRegister, emailDTO, this.setMessagePriority(priority));
    }

    /**
     * Publishes a password recovery message to the email queue.
     *
     * @param user     the user to send the password recovery message to
     * @param password the new password
     */
    public void publishRecoverPasswordMessageEmail(User user, String password) {

        var emailDTO = new RecoverPasswordDTO(
                user.getUserId(),
                user.getEmail(),
                "Redefinir Senha",
                "Sua nova senha é: " + password,
                password
        );
        rabbitTemplate.convertAndSend("", routingKeyRecoverPassword, emailDTO);
    }

    private MessagePostProcessor setMessagePriority(int priority) {
        return message -> {
            MessageProperties properties = message.getMessageProperties();
            properties.setPriority(priority);
            return message;
        };
    }
}
