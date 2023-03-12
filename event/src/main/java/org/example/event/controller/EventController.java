package org.example.event.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import org.example.event.clients.UserClient;
import org.example.event.exception.EventOwnerNotProvidedException;
import org.example.event.model.*;
import org.example.event.repository.EventRepository;
import org.example.event.repository.PassRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/event")
@AllArgsConstructor
public class EventController {
    private final EventRepository eventRepository;
    private final PassRepository passRepository;
    private final UserClient userClient;

    @GetMapping
    public List<Event> getAllEvents() {
//        ResponseEntity<String> response = userClient.validateToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MCwiaWF0IjoxNjc3ODYwNTM5LCJlbWFpbCI6ImFkcmlhbmdvcnNraTIwQGdtYWlsLmNvbSIsInVzZXJuYW1lIjoiQWRyaWFuMTIzIn0.PqIZFrICED5xLAO0P-F-AOr2BTD9xEeK2ppumE7CvN8");
//        System.out.println(response.getStatusCode());


        return eventRepository.findAll();
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
    public void addEvent(@RequestBody @Valid EventDTO eventDto,
                         @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws EventOwnerNotProvidedException {

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
            throw new EventOwnerNotProvidedException();
        }

        Event eventEntity = new Event();

        authorizationHeader = authorizationHeader.substring(7);
        DecodedJWT decodedJwt = JWT.decode(authorizationHeader);
        Claim id = decodedJwt.getClaim("id");

        eventEntity.setOrganizerUserId(id.asInt());
        eventEntity.setName(eventDto.getName());
        eventEntity.setDescription(eventDto.getDescription());
        eventEntity.setStyle(eventDto.getStyle());
        eventEntity.setCountry(eventDto.getCountry());
        eventEntity.setCity(eventDto.getCity());
        eventEntity.setStartingDate(eventDto.getStartingDate());
        eventEntity.setEndingDate(eventDto.getEndingDate());

        eventEntity.setStatus(StatusEnum.DRAFT);

        eventRepository.save(eventEntity);

        eventDto.getPasses().forEach(passDto -> {
            Pass pass = new Pass();
            pass.setEvent(eventEntity);
            pass.setPassName(passDto.getPassName());
            pass.setNumberOfPasses(passDto.getNumberOfPasses());
            pass.setPassType(PassTypeEnum.valueOf(passDto.getPassType()));
            passRepository.save(pass);
        });
    }

    @DeleteMapping
    public void deleteEvent() {

    }

    @PutMapping
    public void editEvent() {

    }
}
