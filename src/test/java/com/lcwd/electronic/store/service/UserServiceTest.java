package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
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
    User user3;
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
        user3 = User.builder()
                .name("Jaddu")
                .email("jaddu@gmail.com")
                .about("This is unit testing")
                .gender("male")
                .imageName("adbc.png")
                .password("jaddu")
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
        Assertions.assertEquals(userDto1.getName(), updatedUser.getName(), "Name not matched !!");
        Assertions.assertEquals(userDto1.getUserId(), updatedUser.getUserId(), "userId not matched !!");

//        Assertions.assertEquals(userDto1, updatedUser);  //fails bz both are different object
    }

    @Test
    public void deleteUserTest() {

        String userId = "xyz";

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user1));

        userService.deleteUser(userId);

        Mockito.verify(userRepository, Mockito.times(1)).delete(user1);
    }

    @Test
    public void getAllUserTest() {

        List<User> userList = Arrays.asList(user1, user2, user3);
        PageImpl<User> page = new PageImpl<>(userList);

        Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

        PageableResponse<UserDto> response = userService.getAllUser(1, 2, "email", "asc");

        Assertions.assertEquals(3, response.getContent().size(), "Number of users are not same as expected !!");
    }

    @Test
    public void getUserByIdTest() {

        String userId = "asdf";

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user1));

        UserDto userDto = userService.getUserById(userId);

        Assertions.assertNotNull(userDto, "User is null !!");
        Assertions.assertEquals("viratkohli18@gmail.com", userDto.getEmail(), "User not found !!");
        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
    }

    @Test
    public void getUserByEmailTest() {

        String email = "rohitsharma45@gmail.com";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user2));

        UserDto userDto = userService.getUserByEmail(email);

        Assertions.assertNotNull(userDto, "User data is null");
        Assertions.assertEquals("Rohit", userDto.getName(), "Names not same !!");
    }

    @Test
    public void searchUserTest() {

        String keyword = "ddu";
        User user4 = User.builder()
                .name("Jaddu")
                .email("jaddu@gmail.com")
                .about("This is unit testing")
                .gender("male")
                .imageName("adbc.png")
                .password("jaddu")
                .build();

        List<User> userList = Arrays.asList(user1, user4);

        Mockito.when(userRepository.findByNameContaining(keyword)).thenReturn(userList);

        List<UserDto> dtoList = userService.searchUser(keyword);

        Assertions.assertEquals(2, dtoList.size(),"size not matched !!");
    }

}
