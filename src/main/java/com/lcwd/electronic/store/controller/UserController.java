package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.helper.ApiConstants;
import com.lcwd.electronic.store.helper.AppConstants;
import com.lcwd.electronic.store.service.FileService;
import com.lcwd.electronic.store.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(ApiConstants.USER_BASE_URL)
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;


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
     * @param pageNumber
     * @param pageSize
     * @return List<UserDto>
     * @author Ankit
     * @apiNote This Api is to get all user data
     */
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
        log.info("Initiated request to get all User details");
        return new ResponseEntity<>(this.service.getAllUser(pageNumber, pageSize, sortBy, sortDir), HttpStatus.FOUND);
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
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
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
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keyword) {
        log.info("Initiated request for search the User with keyword containing:{}", keyword);
        return new ResponseEntity<>(this.service.searchUser(keyword), HttpStatus.FOUND);
    }

    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile image, @PathVariable String userId) throws IOException {
        String imageName = fileService.uploadFile(image, imageUploadPath);
        //update image
        UserDto user = service.getUserById(userId);
        user.setImageName(imageName);
        UserDto userDto = service.updateUser(user, userId);
        //build image response
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).message(AppConstants.IMAGE_UPLOADED).success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }
}
