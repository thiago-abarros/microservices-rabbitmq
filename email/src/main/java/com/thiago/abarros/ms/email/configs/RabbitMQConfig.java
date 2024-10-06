package com.thiago.abarros.ms.email.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * Configuration class for RabbitMQ queues and message converter.
 * This class defines two queues, one for password recovery and another for email registration confirmations,
 * along with a message converter and a retry template for managing retries in case of message failures.
 */
@Configuration
public class RabbitMQConfig {

    // Queue names fetched from the application properties
    @Value("${broker.queue.recover.password}")
    private String queueRecoverPassword;

    @Value("${broker.queue.register}")
    private String queueEmailRegister;

    /**
     * Creates a durable queue for password recovery emails with a TTL (Time-to-Live) of 10 minutes 
     * and the quorum type for high availability.
     * 
     * TTL ensures that messages expire after 10 minutes.
     * The quorum queue type is chosen to ensure message availability in a distributed setup.
     * 
     * @return The configured Queue object for password recovery emails.
     */
    @Bean
    public Queue recoverPasswordQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 600000); // TTL of 10 minutes (in milliseconds)
        args.put("x-queue-type", "quorum"); // Quorum type, high availability
        return new Queue(queueRecoverPassword, true, false, false, args);
    }

    /**
     * Creates a durable queue for email registration confirmations with a maximum priority of 5,
     * and persistent message delivery mode.
     * 
     * Persistent messages ensure that messages are not lost in case of broker failure, 
     * as they are stored on disk.
     * 
     * @return The configured Queue object for email registration confirmations.
     */
    @Bean
    public Queue emailRegisterQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 5); // Max priority
        args.put("x-delivery-mode", 2); // Persistent messages
        return new Queue(queueEmailRegister, true, false, false, args);
    }

    /**
     * Configures a RetryTemplate with exponential backoff and a simple retry policy.
     * 
     * The retry mechanism attempts to reprocess messages up to 5 times in case of failure.
     * The backoff policy is exponential, starting with a 30-second delay and increasing 
     * by a factor of 4, up to a maximum interval of 10 minutes between retries.
     * 
     * This template is used to handle message processing failures and prevents immediate retry loops.
     * 
     * @return The configured RetryTemplate object for handling message retries.
     */
    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        // Configuring exponential backoff policy
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(30000); // 30 seconds initial delay
        backOffPolicy.setMultiplier(4.0); // Exponential multiplier
        backOffPolicy.setMaxInterval(10 * 60 * 1000); // Maximum interval of 10 minutes
        retryTemplate.setBackOffPolicy(backOffPolicy);

        // Configuring simple retry policy with a max of 5 attempts
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(5);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }

    /**
     * Configures a Jackson2JsonMessageConverter to handle JSON message conversion.
     * 
     * This converter serializes message objects to JSON and deserializes JSON back to objects,
     * allowing for structured data exchange between services.
     * 
     * @return The configured Jackson2JsonMessageConverter object for message serialization/deserialization.
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
