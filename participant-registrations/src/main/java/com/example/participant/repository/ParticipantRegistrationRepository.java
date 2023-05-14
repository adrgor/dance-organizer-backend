package com.example.participant.repository;

import com.example.participant.model.Participant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRegistrationRepository extends MongoRepository<Participant, String> {

    List<Participant> findByEventId(int eventId);
}
