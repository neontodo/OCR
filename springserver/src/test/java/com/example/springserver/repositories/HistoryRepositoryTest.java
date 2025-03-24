package com.example.springserver.repositories;

import com.example.springserver.models.History;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class HistoryRepositoryTest {

    @Autowired
    HistoryRepository historyRepository;

    @DisplayName("Test - find history of user by history id")
    @Test
    public void givenUserId_whenFindHistoriesByUserId_thenReturnListOfHistoryEntries(){
        //given - setup test
        History history = History.builder().
                userId(1L).
                medicineId(3L).
                build();
        historyRepository.save(history);

        //when - action
        List<History> historyList = historyRepository.findHistoriesByUserId(1L);

        //then - verify outcome
        Assertions.assertThat(historyList.get(2)).isNotNull();
        Assertions.assertThat(historyList.size()).isEqualTo(3);
    }

    @DisplayName("Test - find history by medicine id")
    @Test
    public void givenMedicineId_whenFindHistoryByMedicineId_thenReturnHistoryEntry(){
        //given - setup test
        History history = History.builder().
                userId(1L).
                medicineId(3L).
                build();
        historyRepository.save(history);

        //when - action
        History historyFound = historyRepository.findHistoriesByMedicineId(1L);

        //then - verify outcome
        Assertions.assertThat(historyFound).isNotNull();
    }

}
