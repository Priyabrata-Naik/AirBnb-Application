package com.codingshuttle.airBnbApp.service;

import com.codingshuttle.airBnbApp.dto.HotelDto;
import com.codingshuttle.airBnbApp.dto.HotelInfoDto;

import java.util.List;

public interface HotelService {

    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long id, HotelDto hotelDto);

    void deleteHotelById(Long id);

    void activateHotel(Long hotelId);

    HotelInfoDto getHotelInfoById(Long hotelId);

    List<HotelDto> getAllHotels();

}
