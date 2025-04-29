package com.example.classroom_db.repository;

import com.example.classroom_db.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    // Поиск по фамилии (без учета регистра)
    List<Teacher> findByLastNameContainingIgnoreCase(String lastName);

    // Поиск по имени и фамилии
    List<Teacher> findByFirstNameAndLastName(String firstName, String lastName);

    // Поиск по части фамилии с сортировкой
    List<Teacher> findByLastNameContainingOrderByLastNameAsc(String lastNamePart);
}