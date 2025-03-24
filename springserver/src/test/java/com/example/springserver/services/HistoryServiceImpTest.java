package com.example.springserver.services;

import com.example.springserver.dto.HistoryDto;
import com.example.springserver.models.History;
import com.example.springserver.models.Medicine;
import com.example.springserver.models.User;
import com.example.springserver.repositories.HistoryRepository;
import com.example.springserver.repositories.MedicineRepository;
import com.example.springserver.repositories.UserRepository;
import com.example.springserver.services.history.HistoryServiceImp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class HistoryServiceImpTest {
    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private HistoryServiceImp historyService;

    private List<History> historyList;
    private History historyEntry1;
    private History historyEntry2;

    private User user;

    private List<Medicine> medicineList;
    private Medicine medicine1;
    private Medicine medicine2;

    @BeforeEach
    public void setup(){
        //setup history related objects
        historyList = new ArrayList<>();
        historyEntry1 = History.builder()
                .historyId(1L)
                .userId(1L)
                .medicineId(1L)
                .build();
        historyEntry2 = History.builder()
                .historyId(2L)
                .userId(1L)
                .medicineId(2L)
                .build();
        historyList.add(historyEntry1);
        historyList.add(historyEntry2);

        //setup user
        user = User.builder()
                .userId(1L)
                .fullName("testFullName")
                .username("testUsername")
                .emailAddress("testMail")
                .password("testPassword")
                .build();

        //setup medicine
        medicineList = new ArrayList<>();
        medicine1 = Medicine.builder().
                id(1L).
                name("testMedicine1").
                build();

        medicine2 = Medicine.builder().
                id(2L).
                name("testMedicine2").
                build();
        medicineList.add(medicine1);
        medicineList.add(medicine2);
    }

    @DisplayName("Test - find all history entries for a user by their ID")
    @Test
    public void givenUserId_whenGetHistoryByUserId_thenReturnAllHistoryEntries(){
        //given - setup test
        Long userId = user.getUserId();
        List<Medicine> foundMedicineHistory;

        //when - action
        when(historyRepository.findHistoriesByUserId(userId)).thenReturn(historyList);
        when(medicineRepository.findMedicineById(1L)).thenReturn(medicine1);
        when(medicineRepository.findMedicineById(2L)).thenReturn(medicine2);

        foundMedicineHistory = new ArrayList<>(historyService.getHistoryByUserId(userId));

        //then - verify outcome
        Assertions.assertThat(foundMedicineHistory).isNotEmpty();
        Assertions.assertThat(foundMedicineHistory.size()).isEqualTo(2);

    }

    @DisplayName("Test - check if the history entry already exists (T)")
    @Test
    public void givenHistoryEntry_whenIsHistoryAlreadyRecorded_thenReturnTrue(){
        //given - setup test
        Long userId = user.getUserId();
        Boolean alreadyExists;

        //when - action
        when(historyRepository.findHistoriesByUserId(userId)).thenReturn(historyList);
        alreadyExists = historyService.isHistoryAlreadyRecorded(historyEntry1);

        //then - verify outcome
        Assertions.assertThat(alreadyExists).isTrue();
    }

    @DisplayName("Test - check if the history entry already exists (F)")
    @Test
    public void givenHistoryEntry_whenIsHistoryAlreadyRecorded_thenReturnFalse(){
        //given - setup test
        Long userId = user.getUserId();
        Boolean alreadyExists;

        History historyEntry3 = History.builder()
                .historyId(3L)
                .userId(1L)
                .medicineId(3L)
                .build();

        //when - action
        when(historyRepository.findHistoriesByUserId(userId)).thenReturn(historyList);
        alreadyExists = historyService.isHistoryAlreadyRecorded(historyEntry3);

        //then - verify outcome
        Assertions.assertThat(alreadyExists).isFalse();
    }
}
