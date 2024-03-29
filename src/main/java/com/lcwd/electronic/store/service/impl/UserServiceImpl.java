package com.lcwd.electronic.store.service.impl;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.AppConstants;
import com.lcwd.electronic.store.helper.PageResponseHelper;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        //Encoding password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        //dto->entity
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
        log.info("Initiated dao call for update the user details with userId :{}", userId);
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userId));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        user.setIsActive(userDto.getIsActive());         //custome fields
        user.setModifiedBy(userDto.getModifiedBy());
        // user.setModifiedOn(userDto.getModifiedOn());   //automatically modified bz of @UpdateTimestamp
        User updatedUser = this.userRepo.save(user);
        log.info("Completed dao call for update the user details with userId :{}", userId);
        return this.mapper.map(updatedUser, UserDto.class);
    }

    /**
     * @author Ankit
     * @implNote This implementation is to delete user
     */
    @Override
    public void deleteUser(String userId) {
        log.info("Initiated dao call for delete the user with userId :{}", userId);
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userId));
        //delete user image
        String fullPath = imageUploadPath + user.getImageName();
        try {
            Path path = Path.of(fullPath);
            Files.delete(path);
        } catch (FileNotFoundException ex) {
            log.info("User image not found in folder");
            ex.getStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //delete user
        this.userRepo.delete(user);
        log.info("Completed dao call for delete the user with userId  :{}", userId);
    }

    /**
     * @author Ankit
     * @implNote This implementation is to get all user data
     */
    @Override
    public PageableResponse<UserDto> getAllUser(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        log.info("Initiated dao call to get all user details with pageNumber:{}, pageSize:{}, sortBy:{}, sortDir:{}", pageNumber, pageSize, sortBy, sortDir);
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest request = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> pageUser = this.userRepo.findAll(request);
        PageableResponse<UserDto> response = PageResponseHelper.getPageableResponse(pageUser, UserDto.class);
        log.info("Completed dao call to get all user details with pageNumber:{}, pageSize:{}, sortBy:{}, sortDir:{}", pageNumber, pageSize, sortBy, sortDir);
        return response;
    }

    /**
     * @author Ankit
     * @implNote This implementation is to get single user data by userId
     */
    @Override
    public UserDto getUserById(String userId) {
        log.info("Initiated dao call to get single user details with userId :{}", userId);
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userId));
        log.info("Completed dao call to get single user details with userId :{}", userId);
        return this.mapper.map(user, UserDto.class);
    }

    /**
     * @author Ankit
     * @implNote This implementation is to get single user data by email
     */
    @Override
    public UserDto getUserByEmail(String email) {
        log.info("Initiated dao call to get user details with email :{}", email);
        User user = this.userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND_WITH_EMAIL + email));
        log.info("Completed dao call to get user details with email :{}", email);
        return this.mapper.map(user, UserDto.class);
    }

    /**
     * @author Ankit
     * @implNote This implementation is for search user by using keyword
     */
    @Override
    public List<UserDto> searchUser(String keyword) {
        log.info("Initiated dao call for search user with keyword containing :{}", keyword);
        List<User> users = this.userRepo.findByNameContaining(keyword);
        List<UserDto> dtos = users.stream().map((user) -> this.mapper.map(user, UserDto.class)).collect(Collectors.toList());
        log.info("Completed dao call for search user with keyword containing :{}", keyword);
        return dtos;
    }

    @Override
    public Optional<User> findUserByEmailOptional(String email) {
        return userRepo.findByEmail(email);
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
