package org.example.registrationdashboardservice.model.registrationform;

import lombok.Data;

import java.util.List;

@Data
public class RegistrationInput {

    private String question;
    private String type;
    private List<String> options;
    private Boolean isRequired;
    private String description;
}