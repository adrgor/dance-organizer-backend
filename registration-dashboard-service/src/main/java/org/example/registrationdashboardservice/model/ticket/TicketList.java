package org.example.registrationdashboardservice.model.ticket;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("tickets")
@Data
public class TicketList {

    @Id
    private String id;

    @NonNull
    private Integer eventId;
    @NonNull
    private List<Ticket> tickets;
}