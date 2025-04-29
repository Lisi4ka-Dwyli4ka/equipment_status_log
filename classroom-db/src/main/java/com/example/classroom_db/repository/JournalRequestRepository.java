package com.example.classroom_db.repository;
import org.springframework.data.jpa.repository.Modifying;

import com.example.classroom_db.entity.JournalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface JournalRequestRepository extends JpaRepository<JournalRequest, Long> {
    // Метод для обновления статуса заявки
    @Modifying

    @Query("SELECT jr FROM JournalRequest jr LEFT JOIN FETCH jr.teacher LEFT JOIN FETCH jr.auditoria LEFT JOIN FETCH jr.equipment ORDER BY jr.date DESC")
    List<JournalRequest> findAllWithDetails();
}
