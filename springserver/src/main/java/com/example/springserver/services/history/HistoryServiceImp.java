package com.example.springserver.services.history;

import com.example.springserver.dto.HistoryDto;
import com.example.springserver.models.History;
import com.example.springserver.models.Medicine;
import com.example.springserver.models.mapper.HistoryMapper;
import com.example.springserver.repositories.HistoryRepository;
import com.example.springserver.repositories.MedicineRepository;
import com.example.springserver.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class HistoryServiceImp implements HistoryService{

    private final HistoryRepository historyRepository;
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;

    @Override
    public List<Medicine> getHistoryByUserId(Long userId) {
        List<History> histories = this.historyRepository.findHistoriesByUserId(userId);
        List<Medicine> medicineList = new ArrayList<>();

        for(int i=0; i<histories.size(); i++){
            History history = histories.get(i);
            Long medicineId = history.getMedicineId();
            Medicine medicine = this.medicineRepository.findMedicineById(medicineId);
            if(Objects.nonNull(medicine)){
                medicineList.add(medicine);
            }
        }
        return medicineList;
    }

    @Override
    public void addHistory(HistoryDto historyDto) {
        History newHistory = HistoryMapper.INSTANCE.historyDtoToHistory(historyDto);
        if(!isHistoryAlreadyRecorded(newHistory)){
            historyRepository.save(newHistory);
        }
    }

    //check to see if history is already saved
    public Boolean isHistoryAlreadyRecorded(History history){
        List<History> searchedHistory = historyRepository.findHistoriesByUserId(history.getUserId());
        Boolean foundMatch = false;

        for(History historySearch : searchedHistory ){
            if (history.getMedicineId() == historySearch.getMedicineId()){
                foundMatch = true;
            }
        }
        return foundMatch;
    }
}
