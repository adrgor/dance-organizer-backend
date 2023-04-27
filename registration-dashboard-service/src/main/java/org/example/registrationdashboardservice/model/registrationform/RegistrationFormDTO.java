package org.example.registrationdashboardservice.model.registrationform;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class RegistrationFormDTO {

    @NonNull
    private Integer eventId;
    @NonNull
    private List<RegistrationInput> inputs;
}
