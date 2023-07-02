package com.example.participant.controller;

import com.example.participant.clients.DashboardClient;
import com.example.participant.clients.EventClient;
import com.example.participant.exception.EmailFieldNotFoundException;
import com.example.participant.exception.InvalidEventOwnerException;
import com.example.participant.exception.ParticipantNotFoundException;
import com.example.participant.messaging.Sender;
import com.example.participant.model.*;
import com.example.participant.repository.ParticipantRegistrationRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/participant-registrations")
@AllArgsConstructor
public class ParticipantRegistrationsController {
    private static final String PARTICIPANT_ID = "participantId";
    private static final String PARTNER_ID = "partnerId";
    private static final String AMOUNT_PAID = "amountPaid";
    private static final String PAYMENT_OPERATION = "paymentOperation";
    private static final String STATUS = "status";
    private static final String EVENT_ID = "eventId";

    private final ParticipantRegistrationRepository participantRegistrationRepository;
    private final EventClient eventClient;
    private final DashboardClient dashboardClient;
    private final Sender sender;

    @GetMapping
    public List<Participant> getParticipantRegistrations(@RequestParam Map<String, String> filter,
                                                         @RequestHeader("Authorization") String jwt) throws InvalidEventOwnerException {
        int eventId = Integer.parseInt(filter.get(EVENT_ID));

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int ownerId = eventClient.getEventOwnerId(eventId, jwt);
        if (ownerId != authenticatedUser.id()) {
            throw new InvalidEventOwnerException();
        }

        List<Participant> participants = participantRegistrationRepository.findByEventId(eventId);
        participants = filterStaticFields(participants, filter);
        participants = filterDynamicFields(participants, filter);

        return participants;
    }

    @PostMapping
    public void addParticipantRegistration(@RequestBody ParticipantRegistrationDTO participantRegistrationDTO) {
        Participant participant = new Participant();
        Map<String, List<String>> formInputs = new HashMap<>();
        participantRegistrationDTO.participant().forEach(input -> formInputs.put(input.question(), input.value()));
        participant.setFormInputs(formInputs);
        participant.setEventId(participantRegistrationDTO.eventId());

        participant = participantRegistrationRepository.save(participant);

        if (!participantRegistrationDTO.partner().isEmpty()) {
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

    @PutMapping
    public void updateParticipantRegistration(@RequestParam(EVENT_ID) int eventId,
                                              @RequestBody ParticipantUpdateDTO participantUpdateDTO,
                                              @RequestHeader("Authorization") String jwt) throws InvalidEventOwnerException, ParticipantNotFoundException {

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int ownerId = eventClient.getEventOwnerId(eventId, jwt);

        if (ownerId != authenticatedUser.id()) {
            throw new InvalidEventOwnerException();
        }

        Participant participant = participantRegistrationRepository.findById(participantUpdateDTO.participantId()).orElseThrow(ParticipantNotFoundException::new);
        participant.setPartnerId(participantUpdateDTO.partnerId());
        participant.setStatus(participantUpdateDTO.status().toUpperCase(Locale.ROOT));
        participant.setFormInputs(participantUpdateDTO.participantData());
        participant.setAmountPaid(participantUpdateDTO.amountPaid());
        participantRegistrationRepository.save(participant);
    }

    @DeleteMapping
    public void removeParticipant(@RequestParam(EVENT_ID) int eventId, @RequestBody List<String> participantIds, @RequestHeader("Authorization") String jwt) throws InvalidEventOwnerException {

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int ownerId = eventClient.getEventOwnerId(eventId, jwt);

        if (ownerId != authenticatedUser.id()) {
            throw new InvalidEventOwnerException();
        }

        participantIds.forEach(participantId -> {
            Participant partner = participantRegistrationRepository.findByPartnerId(participantId);
            if (partner != null) {
                partner.setPartnerId(null);
                participantRegistrationRepository.save(partner);
            }

            participantRegistrationRepository.deleteById(participantId);
        });
    }

    @PostMapping("/email")
    public void sendEmail(@RequestParam(EVENT_ID) int eventId, @RequestBody EmailDTO emailDTO, @RequestHeader("Authorization") String jwt) throws InvalidEventOwnerException, EmailFieldNotFoundException {

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int ownerId = eventClient.getEventOwnerId(eventId, jwt);

        if (ownerId != authenticatedUser.id()) {
            throw new InvalidEventOwnerException();
        }

        RegistrationForm registrationForm = dashboardClient.getRegistrationFormByEventId(eventId);

        String emailFieldName = extractEmailFieldNameFromRegistrationForm(registrationForm);
        List<String> emailList = extractEmailsFromParticipantIds(emailDTO.participantIds(), emailFieldName);

        sender.send(new CustomMessageDTO(emailList, emailDTO.title(), emailDTO.body()));
    }

    private List<Participant> filterStaticFields(List<Participant> participants, Map<String, String> filter) {
        return participants.parallelStream().filter(participant -> participant.getParticipantId()
                .contains(filter.getOrDefault(PARTICIPANT_ID, "")))
                .filter(participant -> participant.getParticipantId().contains(filter.getOrDefault(PARTNER_ID, "")))
                .filter(participant -> participant.getStatus().contains(filter.getOrDefault(STATUS, "").toUpperCase(Locale.ROOT)))
                .filter(participant -> filterPrice(filter.getOrDefault(PAYMENT_OPERATION, "ge"), participant.getAmountPaid(), Float.parseFloat(filter.getOrDefault(AMOUNT_PAID, "-0.01"))))
                .toList();
    }

    private List<Participant> filterDynamicFields(List<Participant> participants, Map<String, String> filter) {
        Stream<Participant> participantStream = participants.stream();

        for (Map.Entry<String, String> entry : filter.entrySet()) {
            if (!isStaticField(entry.getKey())) {
                participantStream = participantStream.filter(participant -> {
                    List<String> value = participant.getFormInputs().getOrDefault(entry.getKey(), List.of());
                    if (value.isEmpty()) return false;
                    else if (value.size() == 1) {
                        return value.get(0).contains(entry.getValue());
                    } else {
                        return value.contains(entry.getValue());
                    }
                });
            }
        }

        return participantStream.toList();
    }

    private boolean isStaticField(String key) {
        return (key.equals(PARTICIPANT_ID) || key.equals(PARTNER_ID) || key.equals(STATUS) || key.equals(PAYMENT_OPERATION) || key.equals(AMOUNT_PAID) || key.equals(EVENT_ID));
    }

    private boolean filterPrice(String operation, float currentAmount, float compareAmount) {
        return switch (operation) {
            case "gt" -> currentAmount > compareAmount;
            case "ge" -> currentAmount >= compareAmount;
            case "eq" -> currentAmount == compareAmount;
            case "le" -> currentAmount <= compareAmount;
            case "lt" -> currentAmount < compareAmount;
            default -> false;
        };
    }

    private String extractEmailFieldNameFromRegistrationForm(RegistrationForm form) throws EmailFieldNotFoundException {
        for (RegistrationInput input : form.inputs()) {
            if (input.type().equals("Email")) {
                return input.question();
            }
        }
        throw new EmailFieldNotFoundException();
    }

    private List<String> extractEmailsFromParticipantIds(List<String> participantIds, String emailFieldName) {
        List<String> emails = new ArrayList<>();
        Iterable<Participant> participants = participantRegistrationRepository.findAllById(participantIds);
        participants.forEach(participant -> emails.add(participant.getFormInputs().get(emailFieldName).get(0)));
        return emails;
    }
}
