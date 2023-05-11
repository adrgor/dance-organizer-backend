package org.example.registrationdashboardservice.model.registrationform;

import lombok.NonNull;

import java.util.List;

public record RegistrationFormDTO(
        @NonNull Integer eventId,
        @NonNull List<RegistrationInput> inputs
) {}
