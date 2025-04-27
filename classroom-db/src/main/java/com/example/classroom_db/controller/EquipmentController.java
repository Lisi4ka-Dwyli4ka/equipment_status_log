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
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    @GetMapping("/search")
    public List<Equipment> searchEquipment(@RequestParam String name) {
        return equipmentRepository.findByNameContainingIgnoreCase(name);
    }

    @PostMapping
    public Equipment createEquipment(@RequestBody Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    @DeleteMapping("/{id}")
    public void deleteEquipment(@PathVariable Long id) {
        equipmentRepository.deleteById(id);
    }
}