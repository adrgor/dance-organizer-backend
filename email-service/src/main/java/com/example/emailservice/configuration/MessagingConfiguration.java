package com.example.emailservice.configuration;

import com.example.emailservice.messaging.MQReceiver;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfiguration {

    @Bean
    public Queue activateUserQueue() {
        return new Queue("activate-user-queue");
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public MQReceiver receiver() {
        return new MQReceiver();
    }
}
