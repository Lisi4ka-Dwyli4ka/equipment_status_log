package com.example.classroom_db.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "auditorias")
@Data
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auditoria_seq")
    @SequenceGenerator(
            name = "auditoria_seq",
            sequenceName = "auditoria_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false)
    private String name;
}