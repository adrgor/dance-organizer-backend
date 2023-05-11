package org.example.event.model;

import java.util.Date;
import java.util.List;

public record EventDTO (
     String eventName,
     List<String> danceStyles,
     String eventType,
     String description,
     String country,
     String city,
     Date startDate,
     Date endDate,
     StatusEnum status
) {}
