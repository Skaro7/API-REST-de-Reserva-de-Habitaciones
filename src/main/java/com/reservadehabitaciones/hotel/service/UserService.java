package com.reservadehabitaciones.hotel.service;

import com.reservadehabitaciones.hotel.dto.request.UserRequest;
import com.reservadehabitaciones.hotel.dto.response.UserResponse;
import com.reservadehabitaciones.hotel.entity.User;
import com.reservadehabitaciones.hotel.exception.ConflictException;
import com.reservadehabitaciones.hotel.exception.ResourceNotFoundException;
import com.reservadehabitaciones.hotel.mapper.UserMapper;
import com.reservadehabitaciones.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse createUser(UserRequest userRequest) {
        userRepository.findByEmail(userRequest.getEmail())
                .ifPresent(u -> { throw new ConflictException("Email ya esta registrado"); });
        User user = userMapper.toEntity(userRequest);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException("Usuario no encontrado"));
        return userMapper.toResponse(user);
    }
}

