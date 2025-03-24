package com.example.springserver.controllers;

import com.example.springserver.models.Medicine;
import com.example.springserver.repositories.MedicineRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test") // Use the test profile
public class MedicineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicineRepository medicineRepository;

    @Test
    public void contextLoads(){
    }

    @DisplayName("Test - get medicine by valid name")
    @Test
    public void testGetMedicineByName_Valid() throws Exception {
        //given - setup
        String medicineName = "augmentin 625 duo tablet";

        //when - action done
        ResultActions response = mockMvc.perform(get("/api/medicine/" + medicineName));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(medicineName)));
    }

    @DisplayName("Test - get medicine by invalid name")
    @Test
    public void testGetMedicineByName_Invalid() throws Exception {
        //given - setup
        String medicineName = "augme";

        //when - action done
        ResultActions response = mockMvc.perform(get("/api/medicine/" + medicineName));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Test - get medicine by valid partial name")
    @Test
    public void testGetMedicineByPartialName_Valid() throws Exception {
        //given - setup
        String partialName = "alex";
        int size = medicineRepository.findAllByNameContaining(partialName).size();

        //when - action done
        ResultActions response = mockMvc.perform(get("/api/medicine/filter/" + partialName));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(size)));
    }

    @DisplayName("Test - get medicine by invalid partial name")
    @Test
    public void testGetMedicineByPartialName_Invalid() throws Exception {
        //given - setup
        String partialName = "gkkglhvgkljg";

        //when - action done
        ResultActions response = mockMvc.perform(get("/api/medicine/filter/" + partialName));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isNotFound());
    }



    /*
    @Test
    public void name() throws Exception {
        //given - setup

        //when - action done

        //then - verify result
    }
    */
}
