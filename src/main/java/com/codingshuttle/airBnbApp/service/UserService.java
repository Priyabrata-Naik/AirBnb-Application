package com.codingshuttle.airBnbApp.service;

import com.codingshuttle.airBnbApp.dto.ProfileUpdateRequestDto;
import com.codingshuttle.airBnbApp.dto.UserDto;
import com.codingshuttle.airBnbApp.entity.User;

public interface UserService {

    User getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();

}
