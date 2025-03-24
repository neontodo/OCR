package com.example.springserver.repositories;

import com.example.springserver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUsersByFullName(String fullName);
    Optional<User> findUsersByUsername(String username);
    Optional<User> findUsersByEmailAddress(String emailAddress);
    User findUserByVerificationToken(String verificationToken);
    Boolean existsUserByEmailAddress(String emailAddress);
}
