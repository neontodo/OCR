package com.example.springserver.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name="users", schema = "licensio")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name="full_name", nullable = false)
    private String fullName;

    @Column(name = "username", nullable=false)
    private String username;

    @Column(name = "email_address", nullable = false, unique = true)
    private String emailAddress;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone", unique = true)
    private String phoneNumber;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "verified", columnDefinition = "tinyint default 0")
    private Boolean verified;
}
