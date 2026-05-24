package com.reservadehabitaciones.hotel.service;

import com.reservadehabitaciones.hotel.dto.request.RoomRequest;
import com.reservadehabitaciones.hotel.entity.Room;
import com.reservadehabitaciones.hotel.exception.ConflictException;
import com.reservadehabitaciones.hotel.mapper.RoomMapper;
import com.reservadehabitaciones.hotel.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomService roomService;

    private RoomRequest testRoomRequest;
    private Room existingRoom;

    @BeforeEach
    void setUp() {
        testRoomRequest = new RoomRequest();
        testRoomRequest.setRoomNumber(101);
        testRoomRequest.setCapacity(2);
        testRoomRequest.setPricePerNight(new BigDecimal("100.00"));

        existingRoom = new Room();
        existingRoom.setId(1L);
        existingRoom.setRoomNumber(101);
        existingRoom.setCapacity(2);
        existingRoom.setPricePerNight(new BigDecimal("100.00"));
    }

    @Test
    void createRoom_duplicateRoomNumber() {
        when(roomRepository.findByRoomNumber(101)).thenReturn(Optional.of(existingRoom));

        assertThrows(ConflictException.class, () ->
                roomService.createRoom(testRoomRequest)
        );

        verify(roomRepository, never()).save(any());
    }
}