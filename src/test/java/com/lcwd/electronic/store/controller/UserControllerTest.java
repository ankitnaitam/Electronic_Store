package com.lcwd.electronic.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper mapper;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void init() {
        user1 = User.builder()
                .name("Virat")
                .email("viratkohli18@gmail.com")
                .about("This is unit testing")
                .gender("male")
                .imageName("abc.png")
                .password("virat18")
                .build();
        user2 = User.builder()
                .name("Rohit")
                .email("rohitsharma45@gmail.com")
                .about("This is unit testing")
                .gender("male")
                .imageName("abc.png")
                .password("rohit45")
                .build();
        user3 = User.builder()
                .name("Jaddu")
                .email("jaddu@gmail.com")
                .about("This is unit testing")
                .gender("male")
                .imageName("adbc.png")
                .password("jaddu")
                .build();
    }

    @Test
    void createUserTest() throws Exception {
        // "/api/users"+Post+data as Json
        // data as Json + status
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(mapper.map(user1, UserDto.class));
        //Actual request for url
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/users/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(user1))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
    }

    @Test
    void updateUserTest() throws Exception {
        // "/api/users/{userId}"+Put+JSON
        String userId = "asf234";
        Mockito.when(userService.updateUser(Mockito.any(), Mockito.anyString())).thenReturn(mapper.map(user1, UserDto.class));
        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/users/" + userId)
                                //header part when security implemented for authorization
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(user1))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
    }

    @Test
    void deleteUserTest() {
    }

    @Test
    void getAllUserTest() {
    }

    @Test
    void getUserByIdTest() {
    }

    @Test
    void getUserByEmailTest() {
    }

    @Test
    void searchUserTest() {
    }

    @Test
    void uploadUserImage() {
    }

    @Test
    void serveUserImageTest() {
    }

    private String convertObjectToJsonString(Object user1) {
        try {
            return new ObjectMapper().writeValueAsString(user1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}