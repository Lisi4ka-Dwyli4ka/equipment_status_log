package com.example.classroom_db.service;

import com.example.classroom_db.entity.JournalRequest;
import com.example.classroom_db.repository.JournalRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalRequestService {

    private final JournalRequestRepository journalRequestRepository;

    @Transactional(readOnly = true)
    public List<JournalRequest> getAllRequests() {
        return journalRequestRepository.findAllWithDetails();
    }

    @Transactional
    public JournalRequest createRequest(JournalRequest request) {
        return journalRequestRepository.save(request);
    }

    @Transactional
    public void deleteRequest(Long id) {
        journalRequestRepository.deleteById(id);
    }
}