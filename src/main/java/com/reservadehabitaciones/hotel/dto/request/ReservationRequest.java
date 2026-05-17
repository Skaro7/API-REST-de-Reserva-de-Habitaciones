package com.reservadehabitaciones.hotel.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Integer roomNumber;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
