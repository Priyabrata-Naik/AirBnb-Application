package com.codingshuttle.airBnbApp.dto;

import com.codingshuttle.airBnbApp.entity.HotelContactInfo;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class HotelDto {

    private Long id;

    private String name;

    private String city;

    private String[] photos;

    private String[] amenities;

    private HotelContactInfo contactInfo;

    private Boolean active;

}
