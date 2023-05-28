package org.example.registrationdashboardservice.model.registrationform;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("registration_forms")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class RegistrationForm {

    @Id
    private String id;

    @NonNull
    private Integer eventId;
    @NonNull
    private List<RegistrationInput> inputs;
    private Boolean isOpen = false;
}