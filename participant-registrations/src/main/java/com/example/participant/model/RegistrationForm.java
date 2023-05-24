package com.example.participant.model;

import java.util.List;

public record RegistrationForm(
        String id,
        Integer eventId,
        List<RegistrationInput> inputs
) {
}