package com.lcwd.electronic.store.service.impl;

import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.AppConstants;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.type.LocalDateTimeType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ModelMapper mapper;

    /**
     * @author Ankit
     * @implNote This implementation is to save user data
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Initiated dao call for save the User details");
        //generate unique Id in String format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        User user = this.mapper.map(userDto, User.class);
        User savedUser = this.userRepo.save(user);
        log.info("Completed dao call for save the User details");
        return this.mapper.map(savedUser, UserDto.class);
    }

    /**
     * @author Ankit
     * @implNote This implementation is to update user data by userId
     */
    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        log.info("Initiated dao call for update the user details with userId:{}", userId);
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userId));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        user.setIsActive(userDto.getIsActive());         //custome fields
        user.setModifiedBy(userDto.getModifiedBy());
       // user.setModifiedOn(userDto.getModifiedOn());
        User updatedUser = this.userRepo.save(user);
        log.info("Completed dao call for update the user details with userId:{}", userId);
        return this.mapper.map(updatedUser, UserDto.class);
    }

    /**
     * @author Ankit
     * @implNote This implementation is to delete user
     */
    @Override
    public void deleteUser(String userId) {
        log.info("Initiated dao call for delete the user with userId:{}", userId);
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userId));
        this.userRepo.delete(user);
        log.info("Completed dao call for delete the user with userId:{}", userId);
    }

    /**
     * @author Ankit
     * @implNote This implementation is to get all user data
     */
    @Override
    public List<UserDto> getAllUser() {
        log.info("Initiated dao call to get all user details");
        List<User> users = this.userRepo.findAll();
        List<UserDto> userDtos = users.stream().map((user) -> this.mapper.map(user, UserDto.class)).collect(Collectors.toList());
        log.info("Completed dao call to get all user details");
        return userDtos;
    }

    /**
     * @author Ankit
     * @implNote This implementation is to get single user data by userId
     */
    @Override
    public UserDto getUserById(String userId) {
        log.info("Initiated dao call to get single user details with userId:{}", userId);
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userId));
        log.info("Completed dao call to get single user details with userId:{}", userId);
        return this.mapper.map(user, UserDto.class);
    }

    /**
     * @author Ankit
     * @implNote This implementation is to get single user data by email
     */
    @Override
    public UserDto getUserByEmail(String email) {
        log.info("Initiated dao call to get user details with email:{}", email);
        User user = this.userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND_WITH_EMAIL + email));
        log.info("Completed dao call to get user details with email:{}", email);
        return this.mapper.map(user, UserDto.class);
    }

    /**
     * @author Ankit
     * @implNote This implementation is for search user by using keyword
     */
    @Override
    public List<UserDto> searchUser(String keyword) {
        log.info("Initiated dao call for search user with keyword containing:{}", keyword);
        List<User> users = this.userRepo.findByNameContaining(keyword);
        List<UserDto> dtos = users.stream().map((user) -> this.mapper.map(user, UserDto.class)).collect(Collectors.toList());
        log.info("Completed dao call for search user with keyword containing:{}", keyword);
        return dtos;
    }

//    // dto to user using builder pattern
//    private User dtoToUser(UserDto userDto) {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .build();
//        return user;
//    }
//
//    // user to dto using builder pattern
//    private UserDto userToDto(User savedUser) {
//        UserDto userDto = UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .about(savedUser.getAbout())
//                .gender(savedUser.getGender())
//                .imageName(savedUser.getImageName())
//                .build();
//        return userDto;
//    }
}
