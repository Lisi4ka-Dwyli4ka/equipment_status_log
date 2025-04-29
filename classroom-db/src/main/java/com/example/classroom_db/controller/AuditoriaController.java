package com.example.classroom_db.controller;

import com.example.classroom_db.entity.Auditoria;
import com.example.classroom_db.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditorias")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaRepository auditoriaRepository;

    @GetMapping
    public List<Auditoria> getAuditorias() {
        return auditoriaRepository.findAll();
    }

    @PostMapping
    public Auditoria addAuditoria(@RequestBody Auditoria auditoria) {
        return auditoriaRepository.save(auditoria);
    }

    @DeleteMapping("/{id}")
    public void deleteAuditoria(@PathVariable Long id) {
        auditoriaRepository.deleteById(id);
    }
}