package com.codingshuttle.airBnbApp.controller;

import com.codingshuttle.airBnbApp.dto.InventoryDto;
import com.codingshuttle.airBnbApp.dto.UpdateInventoryRequestDto;
import com.codingshuttle.airBnbApp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<List<InventoryDto>> getAllInventoryByRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(inventoryService.getAllInventoryByRoom(roomId));
    }

    @PatchMapping("/rooms/{roomId}")
    public ResponseEntity<Void> updateInventory(
            @PathVariable Long roomId,
            @RequestBody UpdateInventoryRequestDto updateInventoryRequestDto
    ) {
        inventoryService.updateInventory(roomId, updateInventoryRequestDto);

        return ResponseEntity.noContent().build();
    }

}
