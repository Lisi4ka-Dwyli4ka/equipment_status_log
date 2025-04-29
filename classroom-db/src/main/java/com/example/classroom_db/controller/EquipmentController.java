package com.example.classroom_db.controller;

import com.example.classroom_db.entity.Equipment;
import com.example.classroom_db.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentRepository equipmentRepository;

    @GetMapping
    public List<Equipment> getEquipment() {
        return equipmentRepository.findAll();
    }

    @PostMapping
    public Equipment addEquipment(@RequestBody Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    @DeleteMapping("/{id}")
    public void deleteEquipment(@PathVariable Long id) {
        equipmentRepository.deleteById(id);
    }
}