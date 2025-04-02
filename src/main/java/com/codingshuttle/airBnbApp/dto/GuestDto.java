package com.codingshuttle.airBnbApp.dto;

import com.codingshuttle.airBnbApp.entity.enums.Gender;
import lombok.Data;

@Data
public class GuestDto {

    private Long id;

    private UserDto user;

    private String name;

    private Gender gender;

    private Integer age;

}
