package org.example.registrationdashboardservice.model.registrationform;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("registration_forms")
@Data
public class RegistrationForm {

    @Id
    private String id;
    private Integer eventId;
    private List<RegistrationInput> inputs;
}