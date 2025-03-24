package com.example.springserver.services.medicine;

import com.example.springserver.models.Medicine;

import java.util.List;
import java.util.Optional;

public interface MedicineService {
    public Optional<Medicine> getMedicineByName(String name);
    public List<Optional<Medicine>> getMedicineByPartialName(String partialName);
}
