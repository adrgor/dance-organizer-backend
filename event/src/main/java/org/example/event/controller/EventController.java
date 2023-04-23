package org.example.event.controller;

import lombok.AllArgsConstructor;
import org.example.event.exception.EventNotFoundException;
import org.example.event.exception.InvalidEventOwnerException;
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
        List<Event> events = eventRepository.findByStatus(StatusEnum.PUBLISHED);
        return EventFilter.filter(events, filter);
    }

    @GetMapping("/my_events")
    public List<Event> getMyEvents(@RequestParam Map<String, String> filter) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Event> events = eventRepository.findByOrganizerUserId(authenticatedUser.id());
        return EventFilter.filter(events, filter);
    }

    @GetMapping("/my_events/last_page")
    public long getMyEventLastPageNumber(@RequestParam("events_per_page") int numberOfEvents) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return eventRepository.countByOrganizerUserId(authenticatedUser.id()) / numberOfEvents;
    }

    @GetMapping("/last_page")
    public long getEventLastPageNumber(@RequestParam("events_per_page") int numberOfEvents) {
        return eventRepository.countByStatus(StatusEnum.PUBLISHED) / numberOfEvents;
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable("id") int eventId) throws EventNotFoundException {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Event event = eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        event.setOwned(event.getOrganizerUserId() == authenticatedUser.id());

        return event;
    }

    @PostMapping
    public void addEvent(@RequestBody @Valid EventDTO eventDTO) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Event eventEntity = new Event();
        eventEntity.setOrganizerUserId(authenticatedUser.id());
        eventEntity.setName(eventDTO.getEventName());
        eventEntity.setDescription(eventDTO.getDescription());
        eventEntity.setDanceStyles(eventDTO.getDanceStyles());
        eventEntity.setEventType(eventDTO.getEventType());
        eventEntity.setCountry(eventDTO.getCountry());
        eventEntity.setCity(eventDTO.getCity());
        eventEntity.setStartingDate(eventDTO.getStartDate());
        eventEntity.setEndingDate(eventDTO.getEndDate());
        eventEntity.setStatus(StatusEnum.DRAFT);

        eventRepository.save(eventEntity);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable("id") int eventId) throws EventNotFoundException, InvalidEventOwnerException {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Event event = eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        if (event.getOrganizerUserId() == authenticatedUser.id()) {
            eventRepository.delete(event);
        } else {
            throw new InvalidEventOwnerException();
        }
    }

    @PutMapping("/change_status/{id}")
    public void publishEvent(@PathVariable("id") int eventId) throws EventNotFoundException, InvalidEventOwnerException {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Event event = eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        if (event.getOrganizerUserId() == authenticatedUser.id()) {
            if (event.getStatus() == StatusEnum.DRAFT) event.setStatus(StatusEnum.PUBLISHED);
            else event.setStatus(StatusEnum.DRAFT);
            eventRepository.save(event);
        } else {
            throw new InvalidEventOwnerException();
        }
    }

    @PutMapping("/edit/{id}")
    public void editEvent(@PathVariable("id") int eventId, @RequestBody EventDTO eventDTO) throws EventNotFoundException {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Event event = eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        if (event.getOrganizerUserId() == authenticatedUser.id()) {
            event.setName(eventDTO.getEventName());
            event.setDescription(eventDTO.getDescription());
            event.setDanceStyles(eventDTO.getDanceStyles());
            event.setEventType(eventDTO.getEventType());
            event.setCountry(eventDTO.getCountry());
            event.setCity(eventDTO.getCity());
            event.setStartingDate(eventDTO.getStartDate());
            event.setEndingDate(eventDTO.getEndDate());
            event.setStatus(eventDTO.getStatus());

            eventRepository.save(event);
        }
    }

    @GetMapping("/owner/{id}")
    public int getEventOwnerId(@PathVariable("id") int eventId) throws EventNotFoundException {
        Event event = eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        return event.getOrganizerUserId();
    }
}
