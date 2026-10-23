package com.passArena.theatre_service.service;

import com.passArena.theatre_service.dto.CreateSeatRequest;
import com.passArena.theatre_service.dto.GenerateSeatRequest;
import com.passArena.theatre_service.dto.SeatResponse;
import com.passArena.theatre_service.dto.SeatRowRequest;
import com.passArena.theatre_service.entity.Screen;
import com.passArena.theatre_service.entity.Seat;
import com.passArena.theatre_service.mapper.SeatMapper;
import com.passArena.theatre_service.repository.ScreenRepository;
import com.passArena.theatre_service.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;
    private final SeatMapper seatMapper;

    public SeatResponse createSeat(UUID screenId, CreateSeatRequest request)
    {
        Screen screen = screenRepository.findById(screenId).orElseThrow(() ->
                new RuntimeException("Screen not found with id: " + screenId));

        Seat seat = Seat.builder()
                .screenId(screen.getId())
                .seatNumber(request.getSeatNumber())
                .rowNumber(request.getRowNumber())
                .seatPrice(request.getSeatPrice())
                .build();

        Seat savedSeat = seatRepository.save(seat);
        return seatMapper.toResponse(savedSeat);
    }

    public Page<SeatResponse> getSeats(UUID screenId, Pageable pageable)
    {
        if(!screenRepository.existsById(screenId)) {
            throw new RuntimeException("Screen not found with id: " + screenId);
        }

        return seatRepository.findByScreenId(screenId, pageable)
                .map(seatMapper::toResponse);
    }

    public SeatResponse updateSeat(UUID seatId, CreateSeatRequest request)
    {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + seatId));

                seat.setSeatNumber(request.getSeatNumber());
                seat.setRowNumber(request.getRowNumber());
                seat.setSeatPrice(request.getSeatPrice());
                seat.setSeatType(request.getSeatType());

        Seat updatedSeat = seatRepository.save(seat);
        return seatMapper.toResponse(updatedSeat);
    }

    public void deleteSeat(UUID seatId)
    {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + seatId));
        seatRepository.delete(seat);
    }

    @Transactional
    public List<SeatResponse> generateSeats(UUID screenId, GenerateSeatRequest request)
    {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new RuntimeException("Screen not found with id: " + screenId));

        List<Seat> seats = new ArrayList<>();

        for(SeatRowRequest row : request.getRows())
        {
            for(int seatNumber = 1 ; seatNumber <= row.getNumberOfSeats() ; seatNumber++)
            {
                String seatName = row.getRowNumber() + seatNumber;

                Seat seat = Seat.builder()
                        .screenId(screen.getId())
                        .seatNumber(seatName)
                        .rowNumber(row.getRowNumber())
                        .seatPrice(row.getPrice())
                        .seatType(row.getSeatType())
                        .build();

                seats.add(seat);
            }
        }

        List<Seat> savedSeats = seatRepository.saveAll(seats);

        return savedSeats.stream()
                .map(seatMapper::toResponse)
                .toList();
    }

    public List<SeatResponse> getAllSeats(UUID screenId)
    {
        if(!screenRepository.existsById(screenId))
        {
            throw new RuntimeException("Screen not found with id:  " + screenId);

        }

        return seatRepository.findByScreenId(screenId)
                .stream()
                .map(seatMapper::toResponse)
                .toList();
    }


}
