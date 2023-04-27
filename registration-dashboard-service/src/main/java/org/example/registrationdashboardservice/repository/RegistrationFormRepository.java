package org.example.registrationdashboardservice.repository;

import org.example.registrationdashboardservice.model.registrationform.RegistrationForm;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RegistrationFormRepository extends MongoRepository<RegistrationForm, String> {

    RegistrationForm findByEventId(int eventId);
}
