package org.example.user.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserDTO {

    private Integer id;

    @NotNull
    @Size(min = 4)
    @Pattern(regexp = "^[a-zA-z0-9_-]*$")
    private String username;

    @NotNull
    @Size(min = 6)
    private String password;

    @NotNull
    @Email
    private String email;

    private String activationUrl;
}
