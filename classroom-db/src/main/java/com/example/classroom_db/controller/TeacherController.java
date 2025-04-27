package com.example.classroom_db.controller;

import com.example.classroom_db.entity.Teacher;
import com.example.classroom_db.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherRepository teacherRepository;

    // Получить всех учителей
    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    // Получить учителей по фамилии (без учета регистра)
    @GetMapping("/search")
    public List<Teacher> getTeachersByLastName(@RequestParam String lastName) {
        return teacherRepository.findByLastNameContainingIgnoreCase(lastName);
    }

    // Получить учителя по ID
    @GetMapping("/{id}")
    public Teacher getTeacherById(@PathVariable Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Учитель не найден"));
    }

    // Добавить нового учителя
    @PostMapping
    public Teacher createTeacher(@RequestBody Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    // Удалить учителя по ID
    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable Long id) {
        teacherRepository.deleteById(id);
    }
}