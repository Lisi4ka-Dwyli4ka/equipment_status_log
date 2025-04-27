package com.example.classroom_db.controller;

import com.example.classroom_db.entity.JournalRequest;
import com.example.classroom_db.service.JournalRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class JournalRequestController {
    private final JournalRequestService journalRequestService;

    @GetMapping
    public List<JournalRequest> getAllRequests() {
        return journalRequestService.getAllRequests();
    }

    @PostMapping
    public JournalRequest createRequest(@RequestBody JournalRequest request) {
        return journalRequestService.createRequest(request);
    }

    @DeleteMapping("/{id}")
    public void deleteRequest(@PathVariable Long id) {
        journalRequestService.deleteRequest(id);
    }
}