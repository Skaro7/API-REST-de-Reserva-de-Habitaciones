package com.reservadehabitaciones.hotel.service;

import com.reservadehabitaciones.hotel.dto.request.UserRequest;
import com.reservadehabitaciones.hotel.entity.User;
import com.reservadehabitaciones.hotel.exception.ConflictException;
import com.reservadehabitaciones.hotel.mapper.UserMapper;
import com.reservadehabitaciones.hotel.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserRequest testUserRequest;
    private User existingUser;

    @BeforeEach
    void setUp() {
        testUserRequest = new UserRequest();
        testUserRequest.setName("Testing User");
        testUserRequest.setEmail("test@usuario1.com");

        existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Existing User");
        existingUser.setEmail("test@usuario1.com");
    }

    @Test
    void createUser_duplicateEmail() {
        when(userRepository.findByEmail("test@usuario1.com")).thenReturn(Optional.of(existingUser));

        assertThrows(ConflictException.class, () ->
                userService.createUser(testUserRequest)
        );

        verify(userRepository, never()).save(any());
    }
}

