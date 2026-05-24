package com.reservadehabitaciones.hotel.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {

    @NotNull
    private Integer roomNumber;

    @NotNull
    @Positive
    private Integer capacity;

    @NotNull
    @Positive
    private BigDecimal pricePerNight;
}
