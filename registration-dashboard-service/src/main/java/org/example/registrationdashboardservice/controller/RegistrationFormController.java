package org.example.registrationdashboardservice.controller;

import lombok.AllArgsConstructor;
import org.example.registrationdashboardservice.clients.EventClient;
import org.example.registrationdashboardservice.exception.InvalidEventOwnerException;
import org.example.registrationdashboardservice.model.AuthenticatedUser;
import org.example.registrationdashboardservice.model.registrationform.RegistrationForm;
import org.example.registrationdashboardservice.model.registrationform.RegistrationFormDTO;
import org.example.registrationdashboardservice.repository.RegistrationFormRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration-dashboard/form")
@AllArgsConstructor
public class RegistrationFormController {

    private final EventClient eventClient;
    private final RegistrationFormRepository registrationFormRepository;

    @GetMapping
    public RegistrationForm getRegistrationFormByEventId(@RequestParam("eventId") int eventId) {
        return registrationFormRepository.findByEventId(eventId);
    }

    @PostMapping
    public void addRegistrationForm(@RequestBody RegistrationFormDTO registrationFormDTO,
                                    @RequestHeader("Authorization") String jwt) throws InvalidEventOwnerException {

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int eventId = registrationFormDTO.eventId();
        int ownerId = eventClient.getEventOwnerId(eventId, jwt);

        RegistrationForm registrationForm = registrationFormRepository.findByEventId(eventId);

        if (registrationForm != null) {
            registrationForm.setInputs(registrationFormDTO.inputs());
        } else {
            registrationForm = new RegistrationForm(registrationFormDTO.eventId(), registrationFormDTO.inputs());
        }

        if (ownerId == authenticatedUser.id())
            registrationFormRepository.save(registrationForm);
        else throw new InvalidEventOwnerException();
    }

    @PutMapping
    public void openRegistration(@RequestParam("eventId") int eventId,
                                 @RequestHeader("Authorization") String jwt) throws InvalidEventOwnerException {

        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int ownerId = eventClient.getEventOwnerId(eventId, jwt);

        if (ownerId == authenticatedUser.id()) {
            RegistrationForm registrationForm = registrationFormRepository.findByEventId(eventId);
            registrationForm.setIsOpen(true);
            registrationFormRepository.save(registrationForm);
        } else throw new InvalidEventOwnerException();
    }
}
