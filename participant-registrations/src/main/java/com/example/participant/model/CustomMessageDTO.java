package com.example.participant.model;

import java.util.List;

public record CustomMessageDTO(
        List<String> emailAddresses,
        String title,
        String body
) {
}