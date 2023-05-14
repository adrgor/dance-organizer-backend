package com.example.participant.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("event")
public interface EventClient {

    @GetMapping("/api/event/owner/{id}")
    int getEventOwnerId(@PathVariable("id") int id, @RequestHeader("Authorization") String jwt);
}