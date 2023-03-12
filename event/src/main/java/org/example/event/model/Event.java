package org.example.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventId;

    private int organizerUserId;

    private String name;

    private String style;

    private String description;

    private String country;

    private String city;

    private Date startingDate;

    private Date endingDate;

    @OneToMany(mappedBy = "event")
    private List<Pass> passes;

    private StatusEnum status;
}
