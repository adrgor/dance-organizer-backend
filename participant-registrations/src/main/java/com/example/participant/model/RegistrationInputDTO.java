package com.example.participant.model;

import java.util.List;

public record RegistrationInputDTO (
    String question,
    List<String> value,
    String role,
    String type
) {}
