package com.example.classroom_db.repository;

import com.example.classroom_db.entity.Equipment;  // Добавьте этот импорт
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByNameContainingIgnoreCase(String name);
}