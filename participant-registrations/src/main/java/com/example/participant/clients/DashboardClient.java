package com.example.participant.clients;

import com.example.participant.model.RegistrationForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("registration-dashboard")
public interface DashboardClient {

    @GetMapping("/api/registration-dashboard/form")
    RegistrationForm getRegistrationFormByEventId(@RequestParam("eventId") int eventId);
}
