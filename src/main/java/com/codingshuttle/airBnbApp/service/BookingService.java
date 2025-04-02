package com.codingshuttle.airBnbApp.service;

import com.codingshuttle.airBnbApp.dto.BookingDto;
import com.codingshuttle.airBnbApp.dto.BookingRequest;
import com.codingshuttle.airBnbApp.dto.GuestDto;
import com.codingshuttle.airBnbApp.dto.HotelReportDto;
import com.stripe.model.Event;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    BookingDto initialiseBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);

    String initiatePayment(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

    String getBookingStatus(Long bookingId);

    List<BookingDto> getAllBookingsByHotelId(Long hotelId);

    HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);

    List<BookingDto> getMyBookings();
}
