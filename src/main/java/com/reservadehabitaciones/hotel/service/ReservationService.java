package com.reservadehabitaciones.hotel.service;

import com.reservadehabitaciones.hotel.dto.request.ReservationRequest;
import com.reservadehabitaciones.hotel.dto.response.ReservationResponse;
import com.reservadehabitaciones.hotel.entity.Reservation;
import com.reservadehabitaciones.hotel.entity.ReservationStatus;
import com.reservadehabitaciones.hotel.entity.Room;
import com.reservadehabitaciones.hotel.exception.ConflictException;
import com.reservadehabitaciones.hotel.exception.ResourceNotFoundException;
import com.reservadehabitaciones.hotel.mapper.ReservationMapper;
import com.reservadehabitaciones.hotel.repository.ReservationRepository;
import com.reservadehabitaciones.hotel.repository.RoomRepository;
import com.reservadehabitaciones.hotel.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.reservadehabitaciones.hotel.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public ReservationResponse createReservation(ReservationRequest reservationRequest) {

        User user = userRepository.findById(reservationRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Room room = roomRepository.findById(Long.valueOf(reservationRequest.getRoomNumber()))
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada"));

        LocalDate startDate = reservationRequest.getStartDate();
        LocalDate endDate = reservationRequest.getEndDate();

        if (reservationRequest.getStartDate().isAfter(reservationRequest.getEndDate())) {
            throw new ConflictException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        if (reservationRequest.getEndDate().isEqual(reservationRequest.getStartDate())) {
            throw new ConflictException("La fecha de inicio y fin no pueden ser iguales");
        }

        if (!reservationRepository.findConflictingReservationsForRoom(room, startDate, endDate).isEmpty()) {
            throw new ConflictException("La habitación ya está reservada en esas fechas");
        }

        if (!reservationRepository.findConflictingReservationsForUser(user, startDate, endDate).isEmpty()) {
            throw new ConflictException("El usuario ya tiene una reserva en esas fechas");
        }

        Reservation reservation = reservationMapper.toEntity(reservationRequest, user, room);
        reservation.setStatus(ReservationStatus.ACTIVE);

        reservationRepository.save(reservation);
        return reservationMapper.toResponse(reservation);
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(reservationMapper::toResponse)
                .toList();
    }

    public ReservationResponse getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));
        return reservationMapper.toResponse(reservation);
    }

    public List<ReservationResponse> getReservationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return reservationRepository.findByUser(user)
                .stream()
                .map(reservationMapper::toResponse)
                .toList();
    }

    @Transactional
    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        if (reservation.getStatus() == ReservationStatus.CANCELLED)
            throw new ConflictException("La reserva no puede ser cancelada porque ya está cancelada");

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledAt(LocalDateTime.now());
        reservationRepository.save(reservation);
    }
}

