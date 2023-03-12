package org.example.user.messaging;

import org.example.user.model.UserDTO;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class Sender {

    @Autowired
    RabbitTemplate template;
    @Autowired
    Queue queue;

    public void send(UserDTO user) {
        this.template.convertAndSend(queue.getName(), user);
    }
}