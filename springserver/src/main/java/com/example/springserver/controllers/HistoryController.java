package com.example.springserver.controllers;

import com.example.springserver.dto.HistoryDto;
import com.example.springserver.models.Medicine;
import com.example.springserver.services.history.HistoryServiceImp;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryServiceImp historyService;

    @ApiOperation("Get all medicine that has been accessed by the user")
    @GetMapping("find-history-by-userId/{userId}")
    public ResponseEntity<List<Medicine>> getMedicineHistoryByUserId(@PathVariable Long userId){
        List<Medicine> medicineList = this.historyService.getHistoryByUserId(userId);
        if(!medicineList.isEmpty()) {
            return ResponseEntity.ok(medicineList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation("Add history after it has been accessed")
    @PostMapping("/add-history")
    public ResponseEntity<String> addHistory(@RequestBody HistoryDto historyDto){
        historyService.addHistory(historyDto);
        return ResponseEntity.ok("History added successfully");
    }

}
