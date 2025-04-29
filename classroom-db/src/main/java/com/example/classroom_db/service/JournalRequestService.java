package com.example.classroom_db.service;

import com.example.classroom_db.entity.*;
import com.example.classroom_db.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalRequestService {

    private final JournalRequestRepository journalRequestRepository;
    private final TeacherRepository teacherRepository;
    private final AuditoriaRepository auditoriaRepository;
    private final EquipmentRepository equipmentRepository;

    @Transactional(readOnly = true)
    public List<JournalRequest> getAllRequestsWithDetails() {
        return journalRequestRepository.findAllWithDetails();
    }

    @Transactional
    public void deleteRequest(Long id) {
        if (!journalRequestRepository.existsById(id)) {
            throw new EntityNotFoundException("Запись журнала с ID " + id + " не найдена");
        }
        journalRequestRepository.deleteById(id);
    }
    @Transactional
    public JournalRequest createRequest(JournalRequest request) {
        // Проверяем существование связанных сущностей
        Teacher teacher = teacherRepository.findById(request.getTeacher().getId())
                .orElseThrow(() -> new EntityNotFoundException("Преподаватель не найден"));
        Auditoria auditoria = auditoriaRepository.findById(request.getAuditoria().getId())
                .orElseThrow(() -> new EntityNotFoundException("Аудитория не найдена"));
        Equipment equipment = equipmentRepository.findById(request.getEquipment().getId())
                .orElseThrow(() -> new EntityNotFoundException("Оборудование не найдено"));

        // Устанавливаем связанные сущности
        request.setTeacher(teacher);
        request.setAuditoria(auditoria);
        request.setEquipment(equipment);

        return journalRequestRepository.save(request);
    }

}