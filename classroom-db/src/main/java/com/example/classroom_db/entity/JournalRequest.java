package com.example.classroom_db.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "journal_requests")
@Data
public class JournalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "journal_request_seq")
    @SequenceGenerator(
            name = "journal_request_seq",
            sequenceName = "journal_request_id_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "auditoria_id", nullable = false)
    private Auditoria auditoria;

    @ManyToOne
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    private String comment;

    @Column(nullable = false)
    private LocalDateTime date;
}