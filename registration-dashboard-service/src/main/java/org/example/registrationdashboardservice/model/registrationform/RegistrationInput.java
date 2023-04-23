package org.example.registrationdashboardservice.model.registrationform;

import lombok.Data;

import java.util.List;

@Data
public class RegistrationInput {

    private String name;
    private RegistrationInputType inputType;
    private List<String> options;
    private Boolean isRequired;
    private String description;
}