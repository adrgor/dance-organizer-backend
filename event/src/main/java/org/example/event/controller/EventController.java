package org.example.event.controller;

import lombok.AllArgsConstructor;
import org.example.event.model.AuthenticatedUser;
import org.example.event.model.Event;
import org.example.event.model.EventDTO;
import org.example.event.model.StatusEnum;
import org.example.event.repository.EventRepository;
import org.example.event.service.EventFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/event")
@AllArgsConstructor
public class EventController {
    private final EventRepository eventRepository;

    @GetMapping
    public List<Event> getEvents(@RequestParam Map<String, String> filter) {

        List<Event> events = eventRepository.findAll();

        return EventFilter.filter(events, filter);
    }

    @GetMapping("/my_events")
    public List<Event> getMyEvents(@RequestParam Map<String, String> filter) {
        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Event> events = eventRepository.findByOrganizerUserId(authenticatedUser.id());
        return EventFilter.filter(events, filter);
    }

    @GetMapping("/last_page")
    public long getEventLastPageNumber(@RequestParam("events_per_page") int numberOfEvents) {
        return eventRepository.count() / numberOfEvents;
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable("id") int eventId) {
        return eventRepository.findById(eventId).get();
    }

    @GetMapping("/organizer/{id}")
    public List<Event> getEventByOrganizerId(@PathVariable("id") int organizerId) {
        return eventRepository.findByOrganizerUserId(organizerId);
    }

    @PostMapping
    public void addEvent(@RequestBody @Valid EventDTO eventDto) {

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Event eventEntity = new Event();
        eventEntity.setOrganizerUserId(authenticatedUser.id());
        eventEntity.setName(eventDto.getEventName());
        eventEntity.setDescription(eventDto.getDescription());
        eventEntity.setDanceStyles(eventDto.getDanceStyles());
        eventEntity.setEventType(eventDto.getEventType());
        eventEntity.setCountry(eventDto.getCountry());
        eventEntity.setCity(eventDto.getCity());
        eventEntity.setStartingDate(eventDto.getStartDate());
        eventEntity.setEndingDate(eventDto.getEndDate());
        eventEntity.setStatus(StatusEnum.DRAFT);

        eventRepository.save(eventEntity);
    }

    @DeleteMapping
    public void deleteEvent() {
        // TODO: Add event removal functionality
    }

    @PutMapping
    public void editEvent() {
        // TODO: Add event editing functionality
    }
}
