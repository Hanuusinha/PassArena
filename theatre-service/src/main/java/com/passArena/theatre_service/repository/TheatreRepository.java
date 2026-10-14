package com.passArena.theatre_service.repository;

import com.passArena.theatre_service.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, UUID> {


}
