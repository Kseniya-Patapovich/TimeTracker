package com.timetracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "project")
public class Project {

    @Id
    @SequenceGenerator(name = "projectSeqGen", sequenceName = "project_seq_gen", allocationSize = 1)
    @GeneratedValue(generator = "projectSeqGen")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProjectStatus projectStatus;

    @Column(name = "deadline")
    //@Temporal(TemporalType.TIME)
    private LocalDateTime deadline;
}
