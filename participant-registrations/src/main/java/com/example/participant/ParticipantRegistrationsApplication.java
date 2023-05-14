package com.example.participant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EnableDiscoveryClient
@EnableFeignClients
public class ParticipantRegistrationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParticipantRegistrationsApplication.class, args);
    }

}
