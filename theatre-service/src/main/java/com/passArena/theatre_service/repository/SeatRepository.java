package com.passArena.theatre_service.repository;

import com.passArena.theatre_service.entity.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID> {

    Page<Seat> findByScreenId(UUID screenId, Pageable pageable);

    List<Seat> findByScreenId(UUID screenId);
}
