package org.example.event.repository;

import org.example.event.model.Event;
import org.example.event.model.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findByStatus(StatusEnum statusEnum);
    List<Event> findByOrganizerUserId(int organizerUserId);
}
