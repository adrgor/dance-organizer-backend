package com.example.participant.messaging;

import com.example.participant.model.CustomMessageDTO;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class Sender {

    @Autowired
    RabbitTemplate template;
    @Autowired
    Queue queue;

    public void send(CustomMessageDTO customMessageDTO) {
        this.template.convertAndSend(queue.getName(), customMessageDTO);
    }
}