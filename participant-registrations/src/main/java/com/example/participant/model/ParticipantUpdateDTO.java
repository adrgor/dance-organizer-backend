package com.example.participant.model;

import java.util.List;
import java.util.Map;

public record ParticipantUpdateDTO(
        int eventId,
        String participantId,
        String partnerId,
        Map<String, List<String>> participantData,
        float amountPaid,
        String status
) {
}
