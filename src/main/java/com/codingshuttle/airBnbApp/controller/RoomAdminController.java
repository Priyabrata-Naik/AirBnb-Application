package com.codingshuttle.airBnbApp.controller;

import com.codingshuttle.airBnbApp.dto.RoomDto;
import com.codingshuttle.airBnbApp.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
public class RoomAdminController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDto> createNewRoom(
            @PathVariable Long hotelId,
            @RequestBody RoomDto roomDto
    ) {
        RoomDto room = roomService.createNewRoom(hotelId, roomDto);

        return new ResponseEntity<>(room, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRoomsInHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getAllRoomsInHotel(hotelId));
    }

    @GetMapping(path = "/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(
            @PathVariable Long hotelId,
            @PathVariable Long roomId
    ) {
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }

    @DeleteMapping(path = "/{roomId}")
    public ResponseEntity<Void> deleteRoomById(
            @PathVariable Long hotelId,
            @PathVariable Long roomId
    ) {
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDto> updateRoomById(
            @PathVariable Long hotelId,
            @PathVariable Long roomId,
            @RequestBody RoomDto roomDto
    ) {
        return ResponseEntity.ok(roomService.updateRoomById(hotelId, roomId, roomDto));
    }

}
