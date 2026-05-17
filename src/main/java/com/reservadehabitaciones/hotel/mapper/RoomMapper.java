package com.reservadehabitaciones.hotel.mapper;

import com.reservadehabitaciones.hotel.dto.request.RoomRequest;
import com.reservadehabitaciones.hotel.dto.response.RoomResponse;
import com.reservadehabitaciones.hotel.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public RoomResponse toResponse(Room room) {
        if (room == null) {
            return null;
        }
        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setRoomNumber(room.getRoomNumber());
        response.setCapacity(room.getCapacity());
        response.setPrice(room.getPricePerNight());
        return response;
    }

    public Room toEntity(RoomRequest request) {

        if (request == null) {
            return null;
        }
        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setCapacity(request.getCapacity());
        room.setPricePerNight(request.getPricePerNight());
        return room;
    }

}
