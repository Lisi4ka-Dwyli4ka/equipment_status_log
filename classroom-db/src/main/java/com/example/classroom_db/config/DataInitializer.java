package com.example.classroom_db.config;

import com.example.classroom_db.entity.Auditoria;
import com.example.classroom_db.entity.Equipment;
import com.example.classroom_db.entity.JournalRequest;
import com.example.classroom_db.entity.Teacher;
import com.example.classroom_db.repository.AuditoriaRepository;
import com.example.classroom_db.repository.EquipmentRepository;
import com.example.classroom_db.repository.JournalRequestRepository;
import com.example.classroom_db.repository.TeacherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initDatabase(TeacherRepository teacherRepository,
                                          AuditoriaRepository auditoriaRepository,
                                          EquipmentRepository equipmentRepository,
                                          JournalRequestRepository journalRequestRepository) {
        return args -> {
            // Проверяем, есть ли уже данные в базе
            if (teacherRepository.count() == 0) {
                // 1. Инициализация преподавателей
                List<Teacher> teachers = Arrays.asList(
                        createTeacher("Вахонин", "Николай Кириллович"),
                        createTeacher("Литвинский", "Игорь Евгеньевич"),
                        createTeacher("Калинин", "Максим Александрович"),
                        createTeacher("Пархимович", "Анна Вячеславовна"),
                        createTeacher("Гардейчик", "Сергей Михайлович")
                );
                teacherRepository.saveAll(teachers);

                // 2. Инициализация аудиторий
                List<Auditoria> auditorias = Arrays.asList(
                        createAuditoria("72"),
                        createAuditoria("73"),
                        createAuditoria("62"),
                        createAuditoria("63"),
                        createAuditoria("52"),
                        createAuditoria("53")
                );
                auditoriaRepository.saveAll(auditorias);

                // 3. Инициализация оборудования
                List<Equipment> equipmentList = Arrays.asList(
                        createEquipment("Стол", "Компьютерный"),
                        createEquipment("Стол", "Преподавательский"),
                        createEquipment("Кресло", "Марис"),
                        createEquipment("Стул", "ИЗО"),
                        createEquipment("Шторы", "Кассетные"),
                        createEquipment("Стол", "Конференционный"),
                        createEquipment("Стол", "Ученический"),
                        createEquipment("Доска", "Маркерная"),
                        createEquipment("Монитор", null),
                        createEquipment("Системный блок", null),
                        createEquipment("Клавиатура", null),
                        createEquipment("Компьютерная мышь", null),
                        createEquipment("Проектор", null)
                );
                equipmentRepository.saveAll(equipmentList);

                // 4. Создаем тестовые заявки в журнал
                JournalRequest request1 = new JournalRequest();
                request1.setTeacher(teachers.get(0));  // Вахонин
                request1.setAuditoria(auditorias.get(0));  // Аудитория 72
                request1.setEquipment(equipmentList.get(0));  // Компьютерный стол
                request1.setComment("Необходима замена монитора");
                request1.setDate(LocalDateTime.now().minusDays(2));

                JournalRequest request2 = new JournalRequest();
                request2.setTeacher(teachers.get(1));  // Литвинский
                request2.setAuditoria(auditorias.get(1));  // Аудитория 73
                request2.setEquipment(equipmentList.get(7));  // Маркерная доска
                request2.setComment("Маркерная доска требует чистки");
                request2.setDate(LocalDateTime.now().minusDays(1));

                journalRequestRepository.saveAll(Arrays.asList(request1, request2));
            }
        };
    }

    private Teacher createTeacher(String lastName, String firstName) {
        Teacher teacher = new Teacher();
        teacher.setLastName(lastName);
        teacher.setFirstName(firstName);
        return teacher;
    }

    private Auditoria createAuditoria(String name) {
        Auditoria auditoria = new Auditoria();
        auditoria.setName(name);
        return auditoria;
    }

    private Equipment createEquipment(String name, String description) {
        Equipment equipment = new Equipment();
        equipment.setName(name);
        equipment.setDescription(description);
        return equipment;
    }
}