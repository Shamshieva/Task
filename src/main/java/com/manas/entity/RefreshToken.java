package com.manas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Entity(name = "refresh_token")
@Getter @Setter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_gen")
    @SequenceGenerator(name = "token_gen", sequenceName = "token_seq", allocationSize = 1)
    private long id;

    @OneToOne
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

}