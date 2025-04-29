    package com.example.classroom_db.entity;

    import jakarta.persistence.*;
    import lombok.Data;

    @Entity
    @Table(name = "equipment")
    @Data
    public class Equipment {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "equipment_seq")
        @SequenceGenerator(
                name = "equipment_seq",
                sequenceName = "equipment_id_seq",
                allocationSize = 1
        )
        private Long id;

        @Column(nullable = false)
        private String name;

        private String description;
    }