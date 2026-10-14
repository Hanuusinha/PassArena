package com.passArena.theatre_service.repository;

import com.passArena.theatre_service.entity.Screen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, UUID> {

    Page<Screen> findByTheatreId(UUID theatreId, Pageable pageable);

    Optional<Screen> findById(UUID uuid);
}
