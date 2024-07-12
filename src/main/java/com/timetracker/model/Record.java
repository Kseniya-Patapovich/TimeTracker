package com.timetracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity(name = "record")
public class Record {
    @Id
    @SequenceGenerator(name = "recordSeqGen", sequenceName = "record_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "recordSeqGen")
    private Long id;

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp startTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp endTime;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
