package com.codingshuttle.airBnbApp.service;

import com.codingshuttle.airBnbApp.entity.Hotel;
import com.codingshuttle.airBnbApp.entity.HotelMinPrice;
import com.codingshuttle.airBnbApp.entity.Inventory;
import com.codingshuttle.airBnbApp.repository.HotelMinPriceRepository;
import com.codingshuttle.airBnbApp.repository.HotelRepository;
import com.codingshuttle.airBnbApp.repository.InventoryRepository;
import com.codingshuttle.airBnbApp.strategy.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PricingUpdateService {

    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final PricingService pricingService;

    //    oper: Scheduler to update the inventory and HotelMinPrice table in every hour
//    @Scheduled(cron = "*/5 * * * * *") // note: Scheduler for 5 sec.
    @Scheduled(cron = "0 0 * * * *") // info: Scheduler for 1 hr.
    public void updatePrices() {
        int page = 0;
        int batchSize = 100;

        while (true) {
            Page<Hotel> hotelPage = hotelRepository.findAll(PageRequest.of(page, batchSize));
            if (hotelPage.isEmpty()) {
                break;
            }
            hotelPage.getContent().forEach(this::updateHotelPrices);

            page++;
        }
    }

    private void updateHotelPrices(Hotel hotel) {
        log.info("Updating hotel price for hotel id: {}", hotel.getId());
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);

        List<Inventory> inventoryList = inventoryRepository.findByHotelAndDateBetween(hotel, startDate, endDate);

        updateInventoryPrices(inventoryList);

        updateHotelMinPrice(hotel, inventoryList, startDate, endDate);
    }

    private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList, LocalDate startDate, LocalDate endDate) {
//        oper: Compute minimum price per day for Hotel
        Map<LocalDate, BigDecimal> dailyMinPrice = inventoryList.stream()
                .collect(Collectors.groupingBy(
                        Inventory::getDate,
                        Collectors.mapping(Inventory::getPrice, Collectors.minBy(Comparator.naturalOrder()))
                ))
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(Map.Entry::getKey, e -> e.getValue().orElse(BigDecimal.ZERO))
                );
//        oper: Prepare HotelPrice entities in bulk
        List<HotelMinPrice> hotelPrices = new ArrayList<>();
        dailyMinPrice.forEach(((date, price) -> {
            HotelMinPrice hotelPrice = hotelMinPriceRepository.findByHotelAndDate(hotel, date)
                    .orElse(new HotelMinPrice(hotel, date));
            hotelPrice.setPrice(price);
            hotelPrices.add(hotelPrice);
        }));

        hotelMinPriceRepository.saveAll(hotelPrices);
    }

    private void updateInventoryPrices(List<Inventory> inventoryList) {
        inventoryList.forEach(inventory -> {
            BigDecimal dynamicPrice = pricingService.calculateDynamicPricing(inventory);
            inventory.setPrice(dynamicPrice);
        });
        inventoryRepository.saveAll(inventoryList);
    }

}
