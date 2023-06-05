package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.UserDto;

import java.util.List;

public interface UserService {

    //create
    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto, String userId);

    //delete user
    void deleteUser(String userId);

    //get all user
    List<UserDto> getAllUser();

    //get single user
    UserDto getUserById(String userId);

    //get user by email
    UserDto getUserByEmail(String email);

    //search user
    List<UserDto> searchUser(String keyword);
}
