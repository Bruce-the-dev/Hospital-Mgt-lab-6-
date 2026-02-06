package com.hospital.model.mapper;

import com.hospital.model.DTO.FeedbackRequestDTO;
import com.hospital.model.DTO.FeedbackResponseDTO;
import com.hospital.model.Feedback;

public class FeedbackMapper {

    public static Feedback toEntity(FeedbackRequestDTO dto) {
        Feedback feedback = new Feedback();
        feedback.setRating(dto.getRating());
        feedback.setComment(dto.getComment());
        return feedback;
    }

    public static FeedbackResponseDTO toDto(Feedback feedback) {
        FeedbackResponseDTO dto = new FeedbackResponseDTO();
        dto.setFeedbackId(feedback.getFeedbackId());
        dto.setPatientId(feedback.getPatient().getPatientId());
        dto.setDoctorId(feedback.getDoctor().getDoctorId());
        dto.setRating(feedback.getRating());
        dto.setComment(feedback.getComment());
        dto.setCreatedDate(feedback.getCreatedDate());
        return dto;
    }
}
