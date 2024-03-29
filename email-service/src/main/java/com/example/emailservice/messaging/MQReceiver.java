package com.example.emailservice.messaging;

import com.example.emailservice.dto.CustomMessageDTO;
import com.example.emailservice.dto.UserDTO;
import com.example.emailservice.email.EmailSender;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

public class MQReceiver {

    @Autowired
    private EmailSender emailSender;

    @RabbitListener(queues = {"activate-user-queue"})
    public void receive(UserDTO userDTO) {
        Map<String, Object> emailParams = new HashMap<>();
        emailParams.put("recipientName", userDTO.getUsername());
        emailParams.put("activationUrl", userDTO.getActivationUrl());

        try {
            emailSender.sendActivationEmail(userDTO.getEmail(), "Dancify - activate account", emailParams);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = {"send-custom-email-queue"})
    public void sendCustomMessage(CustomMessageDTO customMessageDTO) {

        for (String emailAddress : customMessageDTO.emailAddresses()) {
            try {
                emailSender.sendHtmlMessage(emailAddress, customMessageDTO.title(), customMessageDTO.body());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
}
