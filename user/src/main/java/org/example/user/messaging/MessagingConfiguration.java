package org.example.user.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MessagingConfiguration {

    @Bean
    public Queue queue() {
        return new Queue("activate-user-queue");
    }

    @Bean
    public Receiver receiver() {
        return new Receiver();
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Sender activateUserMessageSender() {
        return new Sender();
    }
}