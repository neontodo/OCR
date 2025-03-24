package com.example.springserver.services;

import com.example.springserver.dto.UserDto;
import com.example.springserver.models.User;
import com.example.springserver.models.mapper.UserMapper;
import com.example.springserver.repositories.UserRepository;
import com.example.springserver.services.user.UserServiceImp;
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
public class UserServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImp userService;

    private List<User> userList;
    private User user1;
    private User user2;

    @BeforeEach
    public void setup(){
        userList = new ArrayList<>();
        user1 = User.builder()
                .userId(1L)
                .fullName("testFullName")
                .username("testUsername")
                .emailAddress("testMail")
                .password("testPassword")
                .build();
        user2 = User.builder()
                .userId(2L)
                .fullName("testFullName")
                .username("testUsername")
                .emailAddress("testMail")
                .password("testPassword")
                .build();
        userList.add(user1);
        userList.add(user2);
    }

    @DisplayName("Test - find all users")
    @Test
    public void given_whenGetAllUsers_thenReturnAllUsers(){
        //given - setup test

        //when - action
        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.getAllUsers();

        //then - verify outcome
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result).isEqualTo(userList);
    }

    @DisplayName("Test - get user by their ID")
    @Test
    public void givenUserId_whenGetUserById_thenReturnUser() {
        //given - setup test
        Long userId = 1L;

        //when - action
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        Optional<User> foundUser = userService.getUserById(userId);

        //then - verify outcome
        Assertions.assertThat(foundUser.get()).isEqualTo(user1);
    }

    @DisplayName("Test - get user by their username")
    @Test
    public void givenUsername_whenGetUserByUsername_thenReturnUser() {
        //given - setup test
        String username = "testUsername";

        //when - action
        when(userRepository.findUsersByUsername(username)).thenReturn(Optional.of(user1));
        Optional<User> foundUser = userService.getUserByUsername(username);

        //then - verify outcome
        Assertions.assertThat(foundUser.get()).isEqualTo(user1);
    }

    @DisplayName("Test - get user by their full name")
    @Test
    public void givenFullName_whenGetUserByFullName_thenReturnUser() {
        //given - setup test
        String fullName = "testFullName";

        //when - action
        when(userRepository.findUsersByFullName(fullName)).thenReturn(Optional.of(user1));
        Optional<User> foundUser = userService.getUserByFullName(fullName);

        //then - verify outcome
        Assertions.assertThat(foundUser.get()).isEqualTo(user1);
    }

    @DisplayName("Test - get user by their full name")
    @Test
    public void givenEmail_whenGetUserByEmail_thenReturnUser() {
        //given - setup test
        String email = "testMail";

        //when - action
        when(userRepository.findUsersByEmailAddress(email)).thenReturn(Optional.of(user1));
        Optional<User> foundUser = userService.getUserByEmail(email);

        //then - verify outcome
        Assertions.assertThat(foundUser.get()).isEqualTo(user1);
    }

    /*@DisplayName("Test - register user")
    @Test
    public void givenNewUser_whenRegisterUser_thenReturnOk() {
        //given - setup test
        UserDto userDTO = UserDto.builder()
                .fullName("testFullName3")
                .username("testUsername3")
                .emailAddress("testMail3")
                .password("testPassword3")
                .build();

        User user3 = UserMapper.INSTANCE.userDtoToUser(userDTO);

        //when - action
        when(userRepository.findUsersByUsername(user3.getUsername())).thenReturn(Optional.of(user3));
        userService.registerUser(userDTO);

        Optional<User> foundUser = userRepository.findUsersByUsername(user3.getUsername());

        //then - verify outcome
        Assertions.assertThat(foundUser.get()).isEqualTo(user3);
    }*/

    @DisplayName("Test - register user")
    @Test
    public void givenNewUserDataAndUserId_whenUpdateUser_thenReturnOk() {
        //given - setup test
        UserDto userDTO = UserDto.builder()
                .fullName("testFullName3")
                .username("testUsername3")
                .emailAddress("testMail3")
                .password("testPassword3")
                .build();

        User newUser = UserMapper.INSTANCE.userDtoToUser(userDTO);

        //when - action
        when(userRepository.findById(user1.getUserId())).thenReturn(Optional.ofNullable(newUser));
        userService.updateUser(user1.getUserId(), userDTO);

        Optional<User> foundUser = userRepository.findById(user1.getUserId());

        //then - verify outcome
        Assertions.assertThat(foundUser.get()).isEqualTo(newUser);
    }
}
