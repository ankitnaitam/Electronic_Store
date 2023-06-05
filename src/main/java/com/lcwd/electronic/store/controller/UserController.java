package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.helper.ApiConstants;
import com.lcwd.electronic.store.helper.AppConstants;
import com.lcwd.electronic.store.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(ApiConstants.USER_BASE_URL)
public class UserController {

    @Autowired
    private UserService service;

    //create

    /**
     * @param userDto
     * @return UserDto
     * @author Ankit
     * @apiNote This Api is to save user data
     */
    @PostMapping()
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Initiated request for save the User details");
        UserDto newUser = this.service.createUser(userDto);
        log.info("Completed request for save the User details");
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    //update

    /**
     * @param userDto
     * @param userId
     * @return UserDto
     * @author Ankit
     * @apiNote This Api is to update user data
     */
    @PutMapping(ApiConstants.USER_ID)
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userId") String userId) {
        log.info("Initiated request for update the User details with userId:{}", userId);
        UserDto updatedUser = this.service.updateUser(userDto, userId);
        log.info("Completed request for update the User details with userId:{}", userId);
        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
    }

    //delete

    /**
     * @param userId
     * @return
     * @author Ankit
     * @apiNote This Api is to delete user
     */
    @DeleteMapping(ApiConstants.USER_ID)
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        log.info("Initiated request for delete the User with userId:{}", userId);
        this.service.deleteUser(userId);
        log.info("Completed request for delete the User with userId:{}", userId);
        return new ResponseEntity<>(AppConstants.USER_DELETE + userId, HttpStatus.OK);
    }

    //get all

    /**
     * @return List<UserDto>
     * @author Ankit
     * @apiNote This Api is to get all user data
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser() {
        log.info("Initiated request to get all User details");
        return new ResponseEntity<>(this.service.getAllUser(), HttpStatus.FOUND);
    }

    //get single by id

    /**
     * @param userId
     * @return UserDto
     * @author Ankit
     * @apiNote This Api is to get single user data by userId
     */
    @GetMapping(ApiConstants.USER_ID)
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        log.info("Initiated request to get single User details with userId:{}", userId);
        return new ResponseEntity<>(this.service.getUserById(userId), HttpStatus.FOUND);
    }


    //get by email

    /**
     * @param email
     * @return UserDto
     * @author Ankit
     * @apiNote This Api is to get single user data by email
     */
    @GetMapping(ApiConstants.USER_EMAIL)
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        log.info("Initiated request to get User by email:{}", email);
        return new ResponseEntity<>(this.service.getUserByEmail(email), HttpStatus.FOUND);
    }

    //search user

    /**
     * @param keyword
     * @return List<UserDto>
     * @author Ankit
     * @apiNote This Api is for search user containing keyword
     */
    @GetMapping(ApiConstants.USER_KEYWORD)
    public ResponseEntity<List<UserDto>> searchUser(@RequestParam String keyword) {
        log.info("Initiated request for search the User with keyword containing:{}", keyword);
        return new ResponseEntity<>(this.service.searchUser(keyword), HttpStatus.FOUND);
    }
}
