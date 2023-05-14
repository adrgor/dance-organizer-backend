package org.example.registrationdashboardservice.controller;

import lombok.AllArgsConstructor;
import org.example.registrationdashboardservice.clients.EventClient;
import org.example.registrationdashboardservice.exception.InvalidEventOwnerException;
import org.example.registrationdashboardservice.model.AuthenticatedUser;
import org.example.registrationdashboardservice.model.ticket.TicketList;
import org.example.registrationdashboardservice.model.ticket.TicketListDTO;
import org.example.registrationdashboardservice.repository.TicketRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration-dashboard/ticket")
@AllArgsConstructor
public class TicketController {
    private final EventClient eventClient;

    private final TicketRepository ticketRepository;

    @GetMapping
    public TicketList getTicketsByEventId(@RequestParam("eventId") int eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    @PostMapping
    public void addTicket(@RequestBody TicketListDTO ticketListDTO,
                          @RequestHeader("Authorization") String jwt) throws InvalidEventOwnerException {

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int eventId = ticketListDTO.eventId();
        int ownerId = eventClient.getEventOwnerId(eventId, jwt);

        TicketList ticketList = ticketRepository.findByEventId(eventId);

        if (ticketList != null) {
            ticketList.setTickets(ticketListDTO.tickets());
        } else {
            ticketList = new TicketList(ticketListDTO.eventId(), ticketListDTO.tickets());
        }

        if (ownerId == authenticatedUser.id())
            ticketRepository.save(ticketList);
        else throw new InvalidEventOwnerException();
    }
}