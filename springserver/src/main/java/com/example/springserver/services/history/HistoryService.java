package com.example.springserver.services.history;

import com.example.springserver.dto.HistoryDto;
import com.example.springserver.models.Medicine;

import java.util.List;

public interface HistoryService {
    public List<Medicine> getHistoryByUserId(Long userId);
    public void addHistory(HistoryDto historyDto);
}
