package com.example.springserver.repositories;

import com.example.springserver.models.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    //History findHistoryByHistoryId(Long historyId);
    List<History> findHistoriesByUserId(Long userId);
    History findHistoriesByMedicineId(Long medicineId);
}
