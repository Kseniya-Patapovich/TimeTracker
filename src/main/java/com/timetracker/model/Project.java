package com.timetracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Data
@Entity(name = "project")
public class Project {

    @Id
    @SequenceGenerator(name = "projectSeqGen", sequenceName = "project_seq_gen", allocationSize = 1)
    @GeneratedValue(generator = "projectSeqGen")
    private Long id;

    @Column(name = "project", nullable = false)
    private String name;
}
