package com.example.classroom_db.repository;

import com.example.classroom_db.entity.Auditoria;  // Добавьте этот импорт
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findByNameContainingIgnoreCase(String name);
}