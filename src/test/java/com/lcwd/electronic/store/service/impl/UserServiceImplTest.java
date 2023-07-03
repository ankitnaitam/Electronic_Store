package com.lcwd.electronic.store.service.impl;

import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Autowired
    private ModelMapper mapper;


    @Test
    void createUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getAllUser() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void getUserByEmail() {
    }

    @Test
    void searchUser() {
    }
}