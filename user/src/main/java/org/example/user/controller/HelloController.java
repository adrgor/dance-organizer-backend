package org.example.user.controller;

import org.example.user.messaging.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello-user")
    public String helloUser() {

        return "Hello user";
    }

    @GetMapping("hello-admin")
    public String helloAdmin() {
        return "Hello admin";
    }

    @GetMapping("send")
    public void send() {
//        sender.send("Hello RabbitMQ");
    }
}
