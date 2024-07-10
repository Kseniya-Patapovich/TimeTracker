package com.timetracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Data
@Entity(name = "users")
public class User {
    @Id
    @SequenceGenerator(name = "usersSeqGen", sequenceName = "users_seq_gen", allocationSize = 1)
    @GeneratedValue(generator = "usersSeqGen")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;
}
