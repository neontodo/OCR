package com.example.springserver.models.mapper;

import com.example.springserver.dto.HistoryDto;
import com.example.springserver.models.History;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface HistoryMapper {
    HistoryMapper INSTANCE = Mappers.getMapper(HistoryMapper.class);

    default History historyDtoToHistory(HistoryDto historyDto){
        History history = History.builder().
                userId(historyDto.getUserId()).
                medicineId(historyDto.getMedicineId()).
                build();
        return history;
    }
}
