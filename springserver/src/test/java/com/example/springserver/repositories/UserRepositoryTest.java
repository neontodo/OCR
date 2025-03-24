package com.example.springserver.repositories;

import com.example.springserver.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Test saving a user in repository")
    @Test
    public void givenUser_whenSaveUser_thenOk(){
        //given - setup test
        User user = User.builder()
                .fullName("testFullName")
                .username("testUsername")
                .emailAddress("testMail")
                .password("testPassword")
                .build();

        //when - action
        User userSaved = userRepository.save(user);

        //then - verify outcome
        Assertions.assertThat(userRepository.findUsersByUsername("testUsername")).isNotNull();
    }

    @DisplayName("Test - find all users in the database")
    @Test
    public void givenGetAllUsers_whenGetUsers_thenOk(){
        //given - setup test

        //when - action
        System.out.println(userRepository.findAll());

        //then - verify outcome
        Assertions.assertThat(userRepository).isNotNull();
    }

    @DisplayName("Test - find user by username")
    @Test
    public void givenUsername_whenFindByUsername_thenReturnUser(){
        //given - setup test

        //when - action
        Optional<User> user = userRepository.findUsersByUsername("admin");

        //then - verify outcome
        Assertions.assertThat(user).isNotNull();
    }

    @DisplayName("Test - find user by full name")
    @Test
    public void givenFullName_whenFindByFullName_thenReturnUser(){
        //given - setup test

        //when - action
        Optional<User> user = userRepository.findUsersByFullName("admin");

        //then - verify outcome
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.get().getFullName()).isEqualTo("admin");
    }

    @DisplayName("Test - find user by email address")
    @Test
    public void givenEmail_whenFindByEmail_thenReturnUser(){
        //given - setup test

        //when - action
        Optional<User> user = userRepository.findUsersByEmailAddress("admin@admin.com");

        //then - verify outcome
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.get().getEmailAddress()).isEqualTo("admin@admin.com");
    }

    @DisplayName("Test - user with email address already exists")
    @Test
    public void givenEmail_whenRegisterUser_thenEmailAlreadyInUse(){
        //given - setup test
        User user = User.builder()
                .fullName("testFullName")
                .username("testUsername")
                .emailAddress("testMail")
                .password("testPassword")
                .build();
        userRepository.save(user);

        //when - action
        boolean emailTaken = userRepository.existsUserByEmailAddress("testMail");

        //then - verify outcome
        Assertions.assertThat(emailTaken).isTrue();
    }

    @DisplayName("Test - user with email address already exists")
    @Test
    public void givenEmail_whenRegisterUser_thenEmailAlreadyNotInUse(){
        //given - setup test

        //when - action
        boolean emailTaken = userRepository.existsUserByEmailAddress("testMail");

        //then - verify outcome
        Assertions.assertThat(emailTaken).isFalse();
    }

}
