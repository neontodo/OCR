package com.example.springserver.repositories;

import com.example.springserver.models.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Medicine findMedicineById(Long medicineId);
    Optional<Medicine> findMedicineByName(String name);
    List<Optional<Medicine>> findAllByNameContaining(String partialName);
}
