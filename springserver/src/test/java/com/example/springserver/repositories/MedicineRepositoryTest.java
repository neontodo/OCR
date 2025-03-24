package com.example.springserver.repositories;

import com.example.springserver.models.Medicine;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MedicineRepositoryTest {

    @Autowired
    MedicineRepository medicineRepository;

    @DisplayName("Test - find medicine by id")
    @Test
    public void givenMedicineId_whenFindMedicineById_thenReturnMedicine(){
        //given - setup test

        //when - action
        Medicine medicine = medicineRepository.findMedicineById(1L);

        //then - verify outcome
        Assertions.assertThat(medicine).isNotNull();
        Assertions.assertThat(medicine.getId()).isEqualTo(1L);
    }

    @DisplayName("Test - find medicine by name")
    @Test
    public void givenMedicineName_whenFindMedicineById_thenReturnMedicine(){
        //given - setup test

        //when - action
        Optional<Medicine> medicine = medicineRepository.findMedicineByName("carnosin injection");

        //then - verify outcome
        Assertions.assertThat(medicine).isNotNull();
        Assertions.assertThat(medicine.get().getName()).isEqualTo("carnosin injection");
    }

    @DisplayName("Test - find all medicine by partial name")
    @Test
    public void givenPartialMedicineName_whenFindAllByNameContaining_thenReturnListOfMedicine(){
        //given - setup test

        //when - action
        List<Optional<Medicine>> medicineList = medicineRepository.findAllByNameContaining("alex");

        //then - verify outcome
        Assertions.assertThat(medicineList).isNotNull();
        Assertions.assertThat(medicineList.size()).isEqualTo(148);
    }

    @DisplayName("Test - get empty list of medicine by non-matching partial name")
    @Test
    public void givenPartialMedicineNameNoMatch_whenFindAllByNameContaining_thenReturnEmptyList(){
        //given - setup test

        //when - action
        List<Optional<Medicine>> medicineList = medicineRepository.findAllByNameContaining("sdjghvsdghs");

        //then - verify outcome
        Assertions.assertThat(medicineList).isEmpty();
    }
}
