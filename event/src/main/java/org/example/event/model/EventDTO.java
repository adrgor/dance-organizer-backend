package org.example.event.model;

import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    private String eventName;
    private List<String> danceStyles;
    private String eventType;
    private String description;
    private String country;
    private String city;
    private Date startDate;
    private Date endDate;
}
