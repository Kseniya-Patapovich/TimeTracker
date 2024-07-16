package com.timetracker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "record")
public class Record {
    @Id
    @SequenceGenerator(name = "recordSeqGen", sequenceName = "record_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "recordSeqGen")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime recordDate;

    @Column
    private Integer spent;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private UserTimeTracker user;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
