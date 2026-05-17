package com.reservadehabitaciones.hotel.mapper;

import com.reservadehabitaciones.hotel.dto.request.UserRequest;
import com.reservadehabitaciones.hotel.dto.response.UserResponse;
import com.reservadehabitaciones.hotel.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user){
        if (user == null) {
            return null;
        }
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }

    public User toEntity(UserRequest request) {
        if (request == null) {
            return null;
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        return user;
    }
}
