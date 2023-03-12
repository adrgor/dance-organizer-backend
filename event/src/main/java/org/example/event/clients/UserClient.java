package org.example.event.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("user")
public interface UserClient {

    @PostMapping("/api/user/validate-token")
    ResponseEntity<String> validateToken(@RequestBody String jwt);
}
