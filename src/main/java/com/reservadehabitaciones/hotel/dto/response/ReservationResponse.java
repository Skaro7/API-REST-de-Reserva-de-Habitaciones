package com.reservadehabitaciones.hotel.dto.response;

import com.reservadehabitaciones.hotel.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    private Long id;
    private UserResponse user;
    private RoomResponse room;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private LocalDateTime cancelledAt;
    private ReservationStatus status;


}
