package org.example.registrationdashboardservice.model.ticket;

import java.util.List;

public record TicketListDTO(Integer eventId, List<Ticket> tickets) {
}