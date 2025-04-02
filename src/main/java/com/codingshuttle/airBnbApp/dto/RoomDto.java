package com.codingshuttle.airBnbApp.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomDto {

    private Long id;

    private String type;

    private BigDecimal bashPrice;

    private String[] photos;

    private String[] amenities;

    private Integer totalCount;

    private Integer capacity;

}
