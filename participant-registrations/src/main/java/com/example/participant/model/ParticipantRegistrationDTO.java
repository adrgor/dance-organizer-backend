package com.example.participant.model;

import java.util.List;

public record ParticipantRegistrationDTO(
        int eventId,
        List<RegistrationInputDTO> participant,
        List<RegistrationInputDTO> partner,
        float amountPaid,
        String status
) {
}

