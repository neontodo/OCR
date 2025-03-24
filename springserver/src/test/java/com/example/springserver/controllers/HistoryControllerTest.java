package com.example.springserver.controllers;

import com.example.springserver.dto.HistoryDto;
import com.example.springserver.repositories.HistoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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
public class HistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void contextLoads(){
    }

    @DisplayName("Test - get all history entries of a user by their ID (valid user)")
    @Test
    public void testGetHistoryByUserId_Valid() throws Exception {
        //given - setup
        Long userId = 1L;
        int countHistory = historyRepository.findHistoriesByUserId(1L).size();

        //when - action done
        ResultActions response = mockMvc.perform(get("/api/history/find-history-by-userId/" + userId));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(countHistory)));
    }

    @DisplayName("Test - get all history entries of a user by their ID (invalid user)")
    @Test
    public void testGetHistoryByUserId_Invalid() throws Exception {
        //given - setup
        Long userId = 2143127L;
        int countHistory = historyRepository.findHistoriesByUserId(1L).size();

        //when - action done
        ResultActions response = mockMvc.perform(get("/api/history/find-history-by-userId/" + userId));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Test - add history to user")
    @Test
    public void testAddHistoryToUser() throws Exception {
        //given - setup
        HistoryDto historyDto = HistoryDto.builder()
                .userId(1L)
                .medicineId(5L)
                .build();

        //when - action done
        ResultActions response = mockMvc.perform(post("/api/history/add-history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(historyDto)));


        //then - verify result
        response.andDo(print())
                .andExpect(status().isOk());
    }
}
