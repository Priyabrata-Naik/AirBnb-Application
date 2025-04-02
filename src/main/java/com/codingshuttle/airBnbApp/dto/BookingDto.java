package com.codingshuttle.airBnbApp.dto;

import com.codingshuttle.airBnbApp.entity.Hotel;
import com.codingshuttle.airBnbApp.entity.Room;
import com.codingshuttle.airBnbApp.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDto {

    private Long id;

    private Integer roomsCount;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private BookingStatus bookingStatus;

    private Set<GuestDto> guests;

    private BigDecimal amount;

}
