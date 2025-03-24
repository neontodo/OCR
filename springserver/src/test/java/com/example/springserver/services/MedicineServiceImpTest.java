package com.example.springserver.services;

import com.example.springserver.models.Medicine;
import com.example.springserver.models.User;
import com.example.springserver.repositories.MedicineRepository;
import com.example.springserver.services.medicine.MedicineServiceImp;
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
public class MedicineServiceImpTest {
    @Mock
    private MedicineRepository medicineRepository;

    @InjectMocks
    private MedicineServiceImp medicineService;

    private List<Optional<Medicine>> medicineList;
    private Medicine medicine1;
    private Medicine medicine2;

    @BeforeEach
    public void setup() {
        medicineList = new ArrayList<>();
        medicine1 = Medicine.builder().
                id(1L).
                name("testMedicine1").
                build();

        medicine2 = Medicine.builder().
                id(1L).
                name("testMedicine2").
                build();
        medicineList.add(Optional.ofNullable(medicine1));
        medicineList.add(Optional.ofNullable(medicine2));
    }

    @DisplayName("Test - get medicine by name")
    @Test
    public void givenMedicineName_whenGetMedicineByName_returnMedicine(){
        //given - setup test
        String medicineName = medicine1.getName();

        //when - action
        when(medicineRepository.findMedicineByName(medicineName)).thenReturn(Optional.ofNullable(medicine1));
        Optional<Medicine> medicineFound = medicineService.getMedicineByName(medicineName);

        //then - verify outcome
        Assertions.assertThat(medicineFound.get()).isEqualTo(medicine1);
    }

    @DisplayName("Test - get medicine by name")
    @Test
    public void givenPartialMedicineName_whenGetMedicineByPartialName_returnListOfMedicine(){
        //given - setup test
        String partialName = medicine1.getName();

        //when - action
        when(medicineRepository.findAllByNameContaining(partialName)).thenReturn(medicineList);
        List<Optional<Medicine>> medicineFound = medicineService.getMedicineByPartialName(partialName);

        //then - verify outcome
        Assertions.assertThat(medicineFound).isNotEmpty();
        Assertions.assertThat(medicineFound.size()).isEqualTo(2);
    }

}
