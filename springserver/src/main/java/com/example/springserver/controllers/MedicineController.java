package com.example.springserver.controllers;

import com.example.springserver.models.Medicine;
import com.example.springserver.services.medicine.MedicineServiceImp;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/medicine")
public class MedicineController {

    private final MedicineServiceImp medicineServiceImp;

    @ApiOperation("Get medicine by name")
    @GetMapping("/{name}")
    public ResponseEntity<Medicine> getMedicineByName(@PathVariable String name){
        Optional<Medicine> medicine = medicineServiceImp.getMedicineByName(name);
        return medicine.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation("Get list of medicine matching text")
    @GetMapping("/filter/{partialName}")
    public ResponseEntity<List<Optional<Medicine>>> getMedicineByPartialName(@PathVariable String partialName){
        List<Optional<Medicine>> medicine = medicineServiceImp.getMedicineByPartialName(partialName);
        if(!medicine.isEmpty()){
            return ResponseEntity.ok(medicine);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
