package com.timetracker.security.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "user_security")
public class UserSecurity {
    @Id
    @SequenceGenerator(name = "secSeqGen", sequenceName = "user_security_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "secSeqGen")
    private Long id;

    @Column(name = "user_login")
    private String userLogin;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Roles role;

    @Column(name = "user_id")
    private Long userId;
}
