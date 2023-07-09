package com.lcwd.electronic.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.helper.AppConstants;
import com.lcwd.electronic.store.service.FileService;
import com.lcwd.electronic.store.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper mapper;

    @MockBean
    private FileService fileService;

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
    public void createUserTest() throws Exception {
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
    void deleteUserTest() throws Exception {
        String userId = "12kkd3";
        //Mocking userService.deleteUser(userId) method
        Mockito.doNothing().when(userService).deleteUser(userId);
        //Perform delete request
        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/users/" + userId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(AppConstants.USER_DELETED + userId));
        //verify
        Mockito.verify(userService).deleteUser(userId);
    }

    @Test
    void getAllUserTest() throws Exception {
        UserDto dto1 = mapper.map(user1, UserDto.class);
        UserDto dto2 = mapper.map(user2, UserDto.class);
        UserDto dto3 = mapper.map(user3, UserDto.class);

        PageableResponse<UserDto> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(dto1, dto2, dto3));

        Mockito.when(userService.getAllUser(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pageableResponse);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/users/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    void getUserByIdTest() throws Exception {
        String userId = "23jdmek";
        Mockito.when(userService.getUserById(userId)).thenReturn(mapper.map(user2, UserDto.class));
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/users/" + userId)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    void getUserByEmailTest() throws Exception {
        String email = "rahulsharma23@gmail.com";
        Mockito.when(userService.getUserByEmail(email)).thenReturn(mapper.map(user3, UserDto.class));
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/users/email" + email)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    void searchUserTest() throws Exception {
        String keyword = "rahul";
        UserDto dto1 = mapper.map(user1, UserDto.class);
        UserDto dto2 = mapper.map(user2, UserDto.class);
        UserDto dto3 = mapper.map(user3, UserDto.class);

        Mockito.when(userService.searchUser(keyword)).thenReturn(Arrays.asList(dto1, dto2, dto3));
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/users/keyword/" + keyword)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    void uploadUserImage() throws Exception {
        MultipartFile imageFile = Mockito.any();
        String imageUploadPath = "images/users/";
        String userId="tret234";

        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setImageName("abc.jpg");
        imageResponse.setMessage("uploded");
        imageResponse.setSuccess(true);
        imageResponse.setStatus(HttpStatus.CREATED);

        Mockito.when(fileService.uploadFile(Mockito.any(), Mockito.anyString())).thenReturn(String.valueOf(imageResponse));
        Mockito.when(userService.getUserById(userId)).thenReturn(mapper.map(user1, UserDto.class));
        Mockito.when(userService.updateUser(Mockito.any(), Mockito.anyString())).thenReturn(mapper.map(user1, UserDto.class));

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/users/image/" + user1.getUserId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(AppConstants.IMAGE_UPLOADED + user1.getUserId()));
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