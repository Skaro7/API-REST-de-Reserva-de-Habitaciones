package com.reservadehabitaciones.hotel.service;

import com.reservadehabitaciones.hotel.dto.request.ReservationRequest;
import com.reservadehabitaciones.hotel.dto.response.ReservationResponse;
import com.reservadehabitaciones.hotel.entity.Reservation;
import com.reservadehabitaciones.hotel.entity.Room;
import com.reservadehabitaciones.hotel.entity.User;
import com.reservadehabitaciones.hotel.exception.ConflictException;
import com.reservadehabitaciones.hotel.mapper.ReservationMapper;
import com.reservadehabitaciones.hotel.repository.ReservationRepository;
import com.reservadehabitaciones.hotel.repository.RoomRepository;
import com.reservadehabitaciones.hotel.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationService reservationService;

    private User testUser;
    private Room testRoom;
    private ReservationRequest testReservation;
    private UserService userService;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Usuario testing");
        testUser.setEmail("test@usuario1.com");

        testRoom = new Room();
        testRoom.setId(1L);
        testRoom.setRoomNumber(101);

        testReservation = new ReservationRequest();
        testReservation.setUserId(1L);
        testReservation.setRoomNumber(101);
        testReservation.setStartDate(LocalDate.of(2026, 6, 10));
        testReservation.setEndDate(LocalDate.of(2026, 6, 15));
    }

    @Test
    void createReservation_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(roomRepository.findByRoomNumber(101)).thenReturn(Optional.of(testRoom));
        when(reservationRepository.findConflictingReservationsForRoom(any(), any(), any())).thenReturn(List.of());
        when(reservationRepository.findConflictingReservationsForUser(any(), any(), any())).thenReturn(List.of());
        when(reservationMapper.toEntity(any(), any(), any())).thenReturn(new Reservation());
        when(reservationMapper.toResponse(any())).thenReturn(new ReservationResponse());

        ReservationResponse result = reservationService.createReservation(testReservation);

        assertNotNull(result);
        verify(reservationRepository, times(1)).save(any());
    }

    @Test
    void createReservation_invalidDates() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(roomRepository.findByRoomNumber(101)).thenReturn(Optional.of(testRoom));

        testReservation.setStartDate(LocalDate.of(2026, 6, 15));
        testReservation.setEndDate(LocalDate.of(2026, 6, 10));

        assertThrows(ConflictException.class, () ->
                reservationService.createReservation(testReservation)
        );
    }

    @Test
    void createReservation_roomAlreadyBooked() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(roomRepository.findByRoomNumber(101)).thenReturn(Optional.of(testRoom));

        Reservation existingReservation = new Reservation();
        existingReservation.setId(2L);
        existingReservation.setUser(testUser);
        existingReservation.setRoom(testRoom);
        existingReservation.setStartDate(LocalDate.of(2026, 6, 5));
        existingReservation.setEndDate(LocalDate.of(2026, 6, 20));

        when(reservationRepository.findConflictingReservationsForRoom(testRoom, testReservation.getStartDate(), testReservation.getEndDate()))
                .thenReturn(List.of(existingReservation));

        assertThrows(ConflictException.class, () ->
                reservationService.createReservation(testReservation)
        );

        verify(reservationRepository, never()).save(any());
    }




}
