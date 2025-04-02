package com.codingshuttle.airBnbApp.dto;

import com.codingshuttle.airBnbApp.entity.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileUpdateRequestDto {

    private String name;

    private LocalDate dateOfBirth;

    private Gender gender;

}
