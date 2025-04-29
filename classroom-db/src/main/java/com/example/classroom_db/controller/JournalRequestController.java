package com.example.classroom_db.controller;
import com.example.classroom_db.entity.JournalRequest;
import com.example.classroom_db.service.JournalRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// JournalRequestController.java
@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class JournalRequestController {

    private final JournalRequestService journalRequestService;

    @GetMapping
    public ResponseEntity<List<JournalRequest>> getAllRequests() {
        return ResponseEntity.ok(journalRequestService.getAllRequestsWithDetails());
    }

    @PostMapping
    public ResponseEntity<JournalRequest> addRequest(@RequestBody JournalRequest request) {
        JournalRequest savedRequest = journalRequestService.createRequest(request);
        return ResponseEntity.ok(savedRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        journalRequestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }


}