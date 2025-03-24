package com.example.springserver.controllers;

import com.example.springserver.dto.UserDto;
import com.example.springserver.models.User;
import com.example.springserver.repositories.UserRepository;
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


import javax.transaction.Transactional;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void contextLoads(){
    }

    @DisplayName("Test - Get all users (4)")
    @Test
    public void testGetAllUsers() throws Exception {
        //given
        int countUsers = userRepository.findAll().size();

        //when - action done
        ResultActions response = mockMvc.perform(get("/api/users"));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(countUsers)));

    }

    @DisplayName("Test - Get user by ID")
    @Test
    public void testGetUserById() throws Exception {
        //given - setup
        Long userId = 1L;
        Optional<User> user = userRepository.findById(userId);

        //when - action done
        ResultActions response = mockMvc.perform(get("/api/users/find-user-by-id/" + userId));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.get().getUsername())));
    }

    @DisplayName("Test - Get user by invalid ID")
    @Test
    public void testGetUserById_BadRequest_InvalidId() throws Exception {
        //given - setup
        Long userId = 125L;
        Optional<User> user = userRepository.findById(userId);

        //when - action done
        ResultActions response = mockMvc.perform(get("/api/users/find-user-by-id/" + userId));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Test - get user by their full name")
    @Test
    public void testGetUserByTheirFullName() throws Exception {
        //given - setup
        String fullName = "admin";
        Optional<User> user = userRepository.findUsersByFullName(fullName);

        //when - action done
        ResultActions response = mockMvc.perform(get("/api/users/find-user-by-full-name/" + fullName));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is(user.get().getFullName())));
    }

    @DisplayName("Test - get user by their full name")
    @Test
    public void testGetUserByTheirFullName_NotFound() throws Exception {
        //given - setup
        String fullName = "admin125";
        Optional<User> user = userRepository.findUsersByFullName(fullName);

        //when - action done
        ResultActions response = mockMvc.perform(get("/api/users/find-user-by-full-name/" + fullName));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Test - get user by their username")
    @Test
    public void testGetUserByTheirUsername() throws Exception {
        //given - setup
        String username = "admin";
        Optional<User> user = userRepository.findUsersByUsername(username);

        //when - action done
        ResultActions response = mockMvc.perform(get("/api/users/find-user-by-username/" + username));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.get().getUsername())));
    }

    @DisplayName("Test - get user by their username not found")
    @Test
    public void testGetUserByTheirUsername_NotFound() throws Exception {
        //given - setup
        String username = "admin125";
        Optional<User> user = userRepository.findUsersByUsername(username);

        //when - action done
        ResultActions response = mockMvc.perform(get("/api/users/find-user-by-username/" + username));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Test - get user by their email address")
    @Test
    public void testGetUserByTheirEmail() throws Exception {
        //given - setup
        String email = "admin@admin.com";
        Optional<User> user = userRepository.findUsersByEmailAddress(email);

        //when - action done
        ResultActions response = mockMvc.perform(get("/api/users/find-user-by-email/" + email));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailAddress", is(user.get().getEmailAddress())));
    }

    @DisplayName("Test - get user by their email address, not found")
    @Test
    public void testGetUserByTheirEmail_NotFound() throws Exception {
        //given - setup
        String email = "admin@not.email";
        Optional<User> user = userRepository.findUsersByEmailAddress(email);

        //when - action done
        ResultActions response = mockMvc.perform(get("/api/users/find-user-by-email/" + email));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isNotFound());
    }


    @DisplayName("Test - register valid user")
    @Transactional
    @Test
    public void testRegisterValidUser() throws Exception {
        //given - setup
        UserDto userDto = UserDto.builder()
                .fullName("testFullName")
                .username("testUsername")
                .emailAddress("test@test.com")
                .password("testPassword")
                .build();

        //when - action done
        ResultActions response = mockMvc.perform(post("/api/users/register-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("Test - register invalid user")
    @Transactional
    @Test
    public void testRegisterInvalidUser() throws Exception {
        //given - setup
        UserDto userDto = UserDto.builder()
                .fullName("testFullName")
                .username("testUsername")
                .emailAddress("admin@admin.com")
                .password("testPassword")
                .build();

        //when - action done
        ResultActions response = mockMvc.perform(post("/api/users/register-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Test - update existing user")
    @Transactional
    @Test
    public void testUpdateExistingUser() throws Exception {
        //given - setup
        Long userId = 1L;
        UserDto userDto = UserDto.builder()
                .fullName("testFullName")
                .username("testUsername")
                .emailAddress("test@test.com")
                .password("testPassword")
                .build();

        //when - action done
        ResultActions response = mockMvc.perform(put("/api/users/update-user/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Test - delete existing user")
    @Test
    public void testDeleteExistingUser() throws Exception {
        //given - setup
        Long userId = 5L;

        //when - action done
        ResultActions response = mockMvc.perform(delete("/api/users/delete-user/" + userId));

        //then - verify result
        response.andDo(print())
                .andExpect(status().isOk());
    }

}
