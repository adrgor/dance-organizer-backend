package com.example.participant.controller;

import com.example.participant.clients.EventClient;
import com.example.participant.exception.InvalidEventOwnerException;
import com.example.participant.model.AuthenticatedUser;
import com.example.participant.model.Participant;
import com.example.participant.model.ParticipantRegistrationDTO;
import com.example.participant.repository.ParticipantRegistrationRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/participant-registrations")
@AllArgsConstructor
public class ParticipantRegistrationsController
{
    private final ParticipantRegistrationRepository participantRegistrationRepository;
    private final EventClient eventClient;

    @GetMapping
    public List<Participant> getParticipantRegistrations(@RequestParam("eventId") int eventId,
                                                   @RequestHeader("Authorization") String jwt) throws InvalidEventOwnerException {

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int ownerId = eventClient.getEventOwnerId(eventId, jwt);
        if (ownerId == authenticatedUser.id())
            return participantRegistrationRepository.findByEventId(eventId);
        else throw new InvalidEventOwnerException();
    }

    @PostMapping
    public void addParticipantRegistration(@RequestBody ParticipantRegistrationDTO participantRegistrationDTO) {
        Participant participant = new Participant();
        Map<String, List<String>> formInputs = new HashMap<>();
        participantRegistrationDTO.participant().forEach(input -> formInputs.put(input.question(), input.value()));
        participant.setFormInputs(formInputs);
        participant.setEventId(participantRegistrationDTO.eventId());

        participant = participantRegistrationRepository.save(participant);

        if(!participantRegistrationDTO.partner().isEmpty()) {
            Map<String, List<String>> partnerFormInputs = new HashMap<>();
            participantRegistrationDTO.partner().forEach(input -> partnerFormInputs.put(input.question(), input.value()));
            Participant partner = new Participant();
            partner.setFormInputs(partnerFormInputs);
            partner.setPartnerId(participant.getParticipantId());
            partner.setEventId(participantRegistrationDTO.eventId());
            partner = participantRegistrationRepository.save(partner);
            participant.setPartnerId(partner.getParticipantId());
            participantRegistrationRepository.save(participant);
        }
    }
}
