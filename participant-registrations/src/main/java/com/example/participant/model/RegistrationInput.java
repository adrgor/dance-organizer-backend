package com.example.participant.model;

import java.util.List;

public record RegistrationInput(
        String question,
        String type,
        List<String> options,
        Boolean isRequired,
        String description
) {

}