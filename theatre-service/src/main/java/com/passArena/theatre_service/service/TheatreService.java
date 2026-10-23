package com.passArena.theatre_service.service;

import com.passArena.theatre_service.dto.CreateTheatreRequest;
import com.passArena.theatre_service.dto.TheatreResponse;
import com.passArena.theatre_service.entity.Theatre;
import com.passArena.theatre_service.entity.TheatreStatus;
import com.passArena.theatre_service.mapper.TheatreMapper;
import com.passArena.theatre_service.repository.TheatreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TheatreService {

    private final TheatreRepository theatreRepository;

    private TheatreMapper theatreMapper;

    public TheatreResponse createTheatre(CreateTheatreRequest request)
    {
        Theatre theatre = Theatre.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .status(TheatreStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Theatre savedTheatre = theatreRepository.save(theatre);
        return theatreMapper.toResponse(savedTheatre);
    }

    public Page<TheatreResponse> getAllTheatre(Pageable pageable)
    {
        return theatreRepository.findAll(pageable)
                .map(theatreMapper::toResponse);
    }

    public TheatreResponse getTheatre(UUID theatreId)
    {
        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new RuntimeException("Theatre not found with id: " + theatreId));

        return theatreMapper.toResponse(theatre);
    }

    public TheatreResponse updateTheatre(UUID id, CreateTheatreRequest request)
    {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theatre not found with id: " + id));

        theatre.setName(request.getName());
        theatre.setCity(request.getCity());
        theatre.setAddress(request.getAddress());
        theatre.setUpdatedAt(LocalDateTime.now());

        Theatre updatedTheatre = theatreRepository.save(theatre);
        return theatreMapper.toResponse(updatedTheatre);
    }

    public void deleteTheatre(UUID id)
    {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theatre not found with id: " + id));

        theatre.setStatus(TheatreStatus.INACTIVE);
        theatre.setUpdatedAt(LocalDateTime.now());

        theatreRepository.save(theatre);
    }
}
