package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    User user1;
    User user2;
    UserDto userDto1;

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
        userDto1 = UserDto.builder()
                .name("Ram")
                .email("ram@gmail.com")
                .about("This is updated unit testing")
                .gender("male")
                .imageName("abc.png")
                .password("virat18")
                .build();
    }

    @Test
    public void createUserTest() {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user1);
        UserDto userDto = userService.createUser(mapper.map(user1, UserDto.class));
        Assertions.assertNotNull(userDto);
        System.out.println(userDto.getName());
        Assertions.assertEquals("Virat", userDto.getName());
    }

    @Test
    public void updateUserTest() {
        String id = "abcde";

        System.out.println("user1 before :" + user1.getName());  // Virat

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user1));  // Virat
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user1);  // Ram

        UserDto updatedUser = userService.updateUser(userDto1, id);

        System.out.println("user1 after :" + user1.getName());  // Ram
        System.out.println("updated user using userService method :" + updatedUser.getName());  // Ram
        System.out.println("userDto :" + userDto1.getName());  // Ram

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(userDto1.getName(), updatedUser.getName());
        Assertions.assertEquals(userDto1.getUserId(), updatedUser.getUserId());

//        Assertions.assertEquals(userDto1, updatedUser);  //fails bz both are different object
    }

}
