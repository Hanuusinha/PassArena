package com.passArena.theatre_service.service;

import com.passArena.theatre_service.dto.CreateScreenRequest;
import com.passArena.theatre_service.dto.ScreenResponse;
import com.passArena.theatre_service.entity.Screen;
import com.passArena.theatre_service.entity.Theatre;
import com.passArena.theatre_service.entity.TheatreStatus;
import com.passArena.theatre_service.mapper.ScreenMapper;
import com.passArena.theatre_service.repository.ScreenRepository;
import com.passArena.theatre_service.repository.TheatreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final TheatreRepository theatreRepository;
    private final ScreenMapper screenMapper;

    public ScreenResponse createScreen(UUID theatreId, CreateScreenRequest request)
    {
        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new RuntimeException("Theatre not found"));

        if(theatre.getStatus() != TheatreStatus.ACTIVE)
        {
            throw new RuntimeException("Theatre is not active");
        }

        Screen screen = Screen.builder()
                .theatreId(theatreId)
                .name(request.getName())
                .screenType(request.getScreenType())
                .totalSeats(request.getTotalSeats())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        screen = screenRepository.save(screen);
        return screenMapper.toResponse(screen);
    }

    public Page<ScreenResponse> getScreenByTheatre(UUID theatreId, Pageable pageable)
    {
        if(!theatreRepository.existsById(theatreId))
        {
            throw new RuntimeException("Theatre not found with id:  " + theatreId);
        }

        return screenRepository.findByTheatreId(theatreId,pageable)
                .map(screenMapper::toResponse);
    }

    public ScreenResponse getScreen(UUID screenId)
    {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new RuntimeException("Screen not found with id: " + screenId));

        return screenMapper.toResponse(screen);
    }

    public ScreenResponse updateScreen(UUID screenId, CreateScreenRequest request)
    {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new RuntimeException("Screen not found with id: " + screenId));
        screen.setName(request.getName());
        screen.setScreenType(request.getScreenType());
        screen.setTotalSeats(request.getTotalSeats());
        screen.setUpdatedAt(LocalDateTime.now());

        screen = screenRepository.save(screen);
        return screenMapper.toResponse(screen);
    }

    public void deleteScreen(UUID screenId)
    {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new RuntimeException("Screen not found with id: " + screenId));
        screenRepository.delete(screen);
    }

    public UUID getTheatreId(UUID screenId)
    {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new RuntimeException("Screen not found with id: " + screenId));
        return screen.getTheatreId();
    }
}
