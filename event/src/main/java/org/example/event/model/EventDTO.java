package org.example.event.model;

import reactor.util.annotation.Nullable;

import java.util.Date;
import java.util.List;

public record EventDTO (
     String eventName,
     List<String> danceStyles,
     String eventType,
     String description,
     String country,
     String city,
     @Nullable
     Date startDate,
     @Nullable
     Date endDate,
     String status
) {}
