package com.hospital.service;

import com.hospital.exceptions.ResourceNotFoundException;
import com.hospital.model.DTO.FeedbackRequestDTO;
import com.hospital.model.DTO.FeedbackResponseDTO;
import com.hospital.model.Feedback;
import com.hospital.model.mapper.FeedbackMapper;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.FeedbackRepository;
import com.hospital.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Cacheable(value = "feedback", key = "#id")
    @Transactional(readOnly = true)
    public FeedbackResponseDTO getById(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        return FeedbackMapper.toDto(feedback);
    }

    @CacheEvict(value = "feedback", allEntries = true)
    public FeedbackResponseDTO create(FeedbackRequestDTO dto) {
        Feedback feedback = FeedbackMapper.toEntity(dto);

        java.util.concurrent.CompletableFuture<com.hospital.model.Patient> patientFuture = java.util.concurrent.CompletableFuture
                .supplyAsync(() -> patientRepository.findById(dto.getPatientId())
                        .orElseThrow(() -> new RuntimeException("Patient not found")));

        java.util.concurrent.CompletableFuture<com.hospital.model.Doctor> doctorFuture = java.util.concurrent.CompletableFuture
                .supplyAsync(() -> doctorRepository.findById(dto.getDoctorId())
                        .orElseThrow(() -> new RuntimeException("Doctor not found")));

        java.util.concurrent.CompletableFuture.allOf(patientFuture, doctorFuture).join();

        feedback.setPatient(patientFuture.join());
        feedback.setDoctor(doctorFuture.join());

        return FeedbackMapper.toDto(feedbackRepository.save(feedback));
    }

    @CacheEvict(value = "feedback", key = "#id")
    public void delete(Long id) {
        feedbackRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<FeedbackResponseDTO> getAll(Pageable pageable) {
        return feedbackRepository.findAll(pageable)
                .map(FeedbackMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<FeedbackResponseDTO> getByDoctor(Long doctorId, Pageable pageable) {
        return feedbackRepository
                .findByDoctor_DoctorId(doctorId, pageable)
                .map(FeedbackMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<FeedbackResponseDTO> getByPatient(Long patientId, Pageable pageable) {
        return feedbackRepository
                .findByPatient_PatientId(patientId, pageable)
                .map(FeedbackMapper::toDto);
    }

    @CacheEvict()
    public FeedbackResponseDTO update(Long id, FeedbackRequestDTO dto) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No feedback found with id " + id));
        feedback.setPatient(
                patientRepository.findById(dto.getPatientId())
                        .orElseThrow(() -> new RuntimeException("Patient not found")));

        feedback.setDoctor(
                doctorRepository.findById(dto.getDoctorId())
                        .orElseThrow(() -> new RuntimeException("Doctor not found")));
        if (dto.getComment() != null) {
            feedback.setComment(dto.getComment());
        }
        if (dto.getRating() >= 0) {
            feedback.setRating(dto.getRating());
        }

        // Hibernate will flush changes automatically at transaction commit
        return FeedbackMapper.toDto(feedback);
    }
}
