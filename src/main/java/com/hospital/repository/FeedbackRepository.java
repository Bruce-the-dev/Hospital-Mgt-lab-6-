package com.hospital.repository;

import com.hospital.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Page<Feedback> findByDoctor_DoctorId(Long doctorId, Pageable pageable);

    Page<Feedback> findByPatient_PatientId(Long patientId, Pageable pageable);
}
