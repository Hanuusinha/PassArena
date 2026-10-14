package com.passArena.theatre_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "screens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "theatre_id" , nullable = false)
    private UUID theatreId;

    @Column(nullable = false)
    private String name;

    @Column(name = "screen_type" , nullable = false)
    private String screenType;

    @Column(name = "total_seats" , nullable = false)
    private Integer totalSeats;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
