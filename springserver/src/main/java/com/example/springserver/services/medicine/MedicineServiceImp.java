package com.example.springserver.services.medicine;

import com.example.springserver.models.Medicine;
import com.example.springserver.repositories.MedicineRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MedicineServiceImp implements MedicineService{

    //private final MedicineCSVReader medicineCSVReader;
    private final MedicineRepository medicineRepository;

    @Transactional
    public Optional<Medicine> getMedicineByName(String name){
        return this.medicineRepository.findMedicineByName(name);
    }

    @Transactional
    public List<Optional<Medicine>> getMedicineByPartialName(String partialName){
        List<Optional<Medicine>> medicine = medicineRepository.findAllByNameContaining(partialName);

        return new ArrayList<>(medicine);
    }
}
