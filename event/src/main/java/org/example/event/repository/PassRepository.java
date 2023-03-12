package org.example.event.repository;

import org.example.event.model.Pass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassRepository extends JpaRepository<Pass, Integer> {
}
