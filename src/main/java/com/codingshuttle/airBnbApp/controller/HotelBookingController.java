package com.codingshuttle.airBnbApp.controller;

import com.codingshuttle.airBnbApp.dto.BookingDto;
import com.codingshuttle.airBnbApp.dto.BookingRequest;
import com.codingshuttle.airBnbApp.dto.GuestDto;
import com.codingshuttle.airBnbApp.dto.HotelDto;
import com.codingshuttle.airBnbApp.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping(path = "/init")
    public ResponseEntity<BookingDto> initialiseBooking(@RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(bookingService.initialiseBooking(bookingRequest));
    }

    @PostMapping(path = "/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuests(
            @PathVariable Long bookingId,
            @RequestBody List<GuestDto> guestDtoList
    ) {
        return ResponseEntity.ok(bookingService.addGuests(bookingId, guestDtoList));
    }

    @PostMapping(path = "/{bookingId}/payments")
    public ResponseEntity<Map<String, String>> initiatePayment(
            @PathVariable Long bookingId
    ) {
        String sessionUrl = bookingService.initiatePayment(bookingId);
        return ResponseEntity.ok(Map.of("sessionUrl", sessionUrl));
    }

    @PostMapping(path = "/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(
            @PathVariable Long bookingId
    ) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/{bookingId}/status")
    public ResponseEntity<Map<String, String>> getBookingStatus(@PathVariable Long bookingId) {
        return ResponseEntity.ok(Map.of("status", bookingService.getBookingStatus(bookingId)));
    }

}
