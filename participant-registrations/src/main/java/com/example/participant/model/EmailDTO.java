package com.example.participant.model;

import java.util.List;

public record EmailDTO(
        List<String> participantIds,
        String title,
        String body
) {
}
