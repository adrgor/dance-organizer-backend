package org.example.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    private String name;
    private String style;
    private String description;
    private String country;
    private String city;
    private Date startingDate;
    private Date endingDate;
    private List<PassDTO> passes;
}
