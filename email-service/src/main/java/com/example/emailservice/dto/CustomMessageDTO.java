package com.example.emailservice.dto;

import java.util.List;

public record CustomMessageDTO(List<String> emailAddresses, String title, String body) {
}
