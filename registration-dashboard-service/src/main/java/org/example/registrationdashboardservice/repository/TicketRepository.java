package org.example.registrationdashboardservice.repository;

import org.example.registrationdashboardservice.model.ticket.TicketList;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketRepository extends MongoRepository<TicketList, String> {
    TicketList findByEventId(int eventId);
}
