package com.reservadehabitaciones.hotel.mapper;

import com.reservadehabitaciones.hotel.dto.request.ReservationRequest;
import com.reservadehabitaciones.hotel.dto.response.ReservationResponse;
import com.reservadehabitaciones.hotel.entity.Reservation;
import com.reservadehabitaciones.hotel.entity.Room;
import com.reservadehabitaciones.hotel.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    private UserMapper userMapper;
    private RoomMapper roomMapper;

    public ReservationMapper(UserMapper userMapper, RoomMapper roomMapper) {
        this.userMapper = userMapper;
        this.roomMapper = roomMapper;
    }

    public ReservationResponse toResponse(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        ReservationResponse response = new ReservationResponse();
        response.setId(reservation.getId());
        response.setUser(userMapper.toResponse(reservation.getUser()));
        response.setRoom(roomMapper.toResponse(reservation.getRoom()));
        response.setCreatedAt(reservation.getCreatedAt());
        response.setCancelledAt(reservation.getCancelledAt());
        response.setStartDate(reservation.getStartDate());
        response.setEndDate(reservation.getEndDate());
        response.setStatus(reservation.getStatus());
        return response;
    }

    public Reservation toEntity(ReservationRequest reservationRequest, User user, Room room) {
        if (reservationRequest == null) {
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setStartDate(reservationRequest.getStartDate());
        reservation.setEndDate(reservationRequest.getEndDate());
        return reservation;

    }

}
