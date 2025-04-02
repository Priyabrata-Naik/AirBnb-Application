package com.codingshuttle.airBnbApp.service;

import com.codingshuttle.airBnbApp.dto.*;
import com.codingshuttle.airBnbApp.entity.Hotel;
import com.codingshuttle.airBnbApp.entity.Inventory;
import com.codingshuttle.airBnbApp.entity.Room;
import com.codingshuttle.airBnbApp.entity.User;
import com.codingshuttle.airBnbApp.exception.ResourceNotFoundException;
import com.codingshuttle.airBnbApp.repository.HotelMinPriceRepository;
import com.codingshuttle.airBnbApp.repository.InventoryRepository;
import com.codingshuttle.airBnbApp.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.codingshuttle.airBnbApp.util.AppUtils.getCurrentUser;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImplementation implements InventoryService {
    private final RoomRepository roomRepository;

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final HotelMinPriceRepository hotelMinPriceRepository;

    @Override
    public void initializeRoomForYear(Room room) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);

        for (; !today.isAfter(endDate); today = today.plusDays(1)) {
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .city(room.getHotel().getCity())
                    .bookedCount(0)
                    .reservedCount(0)
                    .totalCount(room.getTotalCount())
                    .price(room.getBashPrice())
                    .closed(false)
                    .date(today)
                    .surgeFactor(BigDecimal.ONE)
                    .build();

            inventoryRepository.save(inventory);
        }
    }

    @Override
    public void deleteAllInventories(Room room) {
        log.info("Deleting with inventories of room with id: {}", room.getId());
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest) {
        log.info("Searching hotels for {} city, from {} to {}",
                hotelSearchRequest.getCity(),
                hotelSearchRequest.getStartDate(),
                hotelSearchRequest.getEndDate()
        );
        Pageable pageable = PageRequest.of(hotelSearchRequest.getPage(), hotelSearchRequest.getSize());
        long dateCount = ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate(), hotelSearchRequest.getEndDate()) + 1;

//        note: business logic - 90 days
        Page<HotelPriceDto> hotelPage = hotelMinPriceRepository.findHotelsWithAvailableInventory(
                hotelSearchRequest.getCity(),
                hotelSearchRequest.getStartDate(),
                hotelSearchRequest.getEndDate(),
                hotelSearchRequest.getRoomsCount(),
                dateCount,
                pageable
        );

        return hotelPage;
    }

    @Override
    public List<InventoryDto> getAllInventoryByRoom(Long roomId) {
        log.info("Getting all inventory by room for room with id: {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
        User user = getCurrentUser();
        if (!user.equals(room.getHotel().getOwner())) {
            throw new AccessDeniedException("You are not the owner of room with id: " + roomId);
        }

        return inventoryRepository.findByRoomOrderByDate(room)
                .stream()
                .map((element) -> modelMapper.map(element, InventoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto) {
        log.info("Updating all inventory by room for room with id: {} between date range: {} - {}",
                roomId,
                updateInventoryRequestDto.getStartDate(),
                updateInventoryRequestDto.getEndDate()
        );
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
        User user = getCurrentUser();
        if (!user.equals(room.getHotel().getOwner())) {
            throw new AccessDeniedException("You are not the owner of room with id: " + roomId);
        }

        inventoryRepository.getInventoryAndLockBeforeUpdates(
                roomId,
                updateInventoryRequestDto.getStartDate(),
                updateInventoryRequestDto.getEndDate()
        );

        inventoryRepository.updateInventory(
                roomId,
                updateInventoryRequestDto.getStartDate(),
                updateInventoryRequestDto.getEndDate(),
                updateInventoryRequestDto.getClosed(),
                updateInventoryRequestDto.getSurgeFactor()
        );

    }

}
