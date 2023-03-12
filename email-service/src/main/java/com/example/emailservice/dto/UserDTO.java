package com.example.emailservice.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String activationUrl;
}
