package com.timetracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity(name = "record")
public class Record {
    @Id
    @SequenceGenerator(name = "recordSeqGen", sequenceName = "record_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "recordSeqGen")
    private Long id;

    @Column(nullable = false)
    @PastOrPresent
    private LocalDate recordDate;

    @Column
    private Integer spent;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private TimeTrackerUser user;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
