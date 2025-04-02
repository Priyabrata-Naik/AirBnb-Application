package com.codingshuttle.airBnbApp.service;

import com.codingshuttle.airBnbApp.dto.RoomDto;
import com.codingshuttle.airBnbApp.entity.Hotel;
import com.codingshuttle.airBnbApp.entity.Room;
import com.codingshuttle.airBnbApp.entity.User;
import com.codingshuttle.airBnbApp.exception.ResourceNotFoundException;
import com.codingshuttle.airBnbApp.exception.UnAuthorisedException;
import com.codingshuttle.airBnbApp.repository.HotelRepository;
import com.codingshuttle.airBnbApp.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.codingshuttle.airBnbApp.util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImplementation implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;

    @Override
    public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {
        log.info("Creating a room in hotel with Id: {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("user : {}, hotel owner: {}", user, hotel.getOwner());

        if (!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user doesn't own this hotel with id: " + hotelId);
        }

        Room room = modelMapper.map(roomDto, Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);

        if (hotel.getActive()) {
            inventoryService.initializeRoomForYear(room);
        }

        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("Getting all room in hotel with Id: {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user doesn't own this hotel with id: " + hotelId);
        }

        return hotel.getRooms()
                .stream()
                .map((room -> modelMapper.map(room, RoomDto.class)))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("Getting the room in with Id: {}", roomId);
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    @Transactional
    public void deleteRoomById(Long roomId) {
        log.info("Deleting the room in with Id: {}", roomId);
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.equals(room.getHotel().getOwner())) {
            throw new UnAuthorisedException("This user doesn't own this room with id: " + roomId);
        }

        inventoryService.deleteAllInventories(room);
        roomRepository.deleteById(roomId);

    }

    @Override
    @Transactional
    public RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto) {
        /*log.info("Updating the room with id: {}", roomId);

        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        User user = getCurrentUser();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user doesn't own this hotel with id: " + hotelId);
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

        modelMapper.map(roomDto, room);
        room.setId(roomId);

//        Todo: If price or inventory is updated, then update the inventory for this room

        room = roomRepository.save(room);

        return modelMapper.map(room, RoomDto.class);*/

        log.info("Updating the room with ID: {}", roomId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));

        User user = getCurrentUser();
        if (!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: " + hotelId);
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));

        modelMapper.map(roomDto, room);
        room.setId(roomId);

//        TODO: if price or inventory is updated, then update the inventory for this room
        room = roomRepository.save(room);

        return modelMapper.map(room, RoomDto.class);
    }
}
