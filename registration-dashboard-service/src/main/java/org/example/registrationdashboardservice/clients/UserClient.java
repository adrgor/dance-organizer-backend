package org.example.registrationdashboardservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("user")
public interface UserClient {

    @PostMapping("/api/user/validate-token")
    boolean validateToken(@RequestBody String jwt);
}