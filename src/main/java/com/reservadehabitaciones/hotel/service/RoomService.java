package com.reservadehabitaciones.hotel.service;

import com.reservadehabitaciones.hotel.dto.request.RoomRequest;
import com.reservadehabitaciones.hotel.dto.response.RoomResponse;
import com.reservadehabitaciones.hotel.entity.Room;
import com.reservadehabitaciones.hotel.exception.ConflictException;
import com.reservadehabitaciones.hotel.exception.ResourceNotFoundException;
import com.reservadehabitaciones.hotel.mapper.RoomMapper;
import com.reservadehabitaciones.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomResponse createRoom(RoomRequest roomRequest) {
        roomRepository.findByRoomNumber(roomRequest.getRoomNumber())
                .ifPresent(r -> { throw new ConflictException("Numero de habitacion ya existe"); });
        var room = roomMapper.toEntity(roomRequest);
        var savedRoom = roomRepository.save(room);
        return roomMapper.toResponse(savedRoom);
    }

    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(roomMapper::toResponse)
                .toList();
    }

    public RoomResponse getRoom(Long id) {
        Room room =  roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion no encontrada"));
        return roomMapper.toResponse(room);
    }
}
