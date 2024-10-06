package com.thiago.abarros.ms.email.configs;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${broker.queue.recover.password}")
    private String queueRecoverPassword;

    @Value("${broker.queue.register}")
    private String queueEmailRegister;

    @Bean
    public Queue recoverPasswordQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 10);
        return new Queue(queueRecoverPassword, true, false, false, args);
    }

    @Bean
    public Queue emailRegisterQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 5);
        return new Queue(queueEmailRegister, true, false, false, args);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
