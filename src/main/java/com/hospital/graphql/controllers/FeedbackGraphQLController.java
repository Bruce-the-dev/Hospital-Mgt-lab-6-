package com.hospital.graphql.controllers;

import com.hospital.model.DTO.FeedbackRequestDTO;
import com.hospital.model.DTO.FeedbackResponseDTO;
import com.hospital.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;

@Controller
@RequiredArgsConstructor
public class FeedbackGraphQLController {

    private final FeedbackService feedbackService;

    @QueryMapping
    public FeedbackResponseDTO feedbackById(@Argument Long id) {

        return feedbackService.getById(id);
    }

    @QueryMapping
    public Page<FeedbackResponseDTO> allFeedback(
            @Argument Integer page,
            @Argument Integer size
    ) {
        int p = page != null ? page : 0;
        int s = size != null ? size : 10;


        return feedbackService.getAll(PageRequest.of(p, s));

    }

    @QueryMapping
    public Page<FeedbackResponseDTO> feedbackByDoctor(
            @Argument Long doctorId,
            @Argument Integer page,
            @Argument Integer size
    ) {
        int p = page != null ? page : 0;
        int s = size != null ? size : 10;
        return feedbackService.getByDoctor(doctorId, PageRequest.of(p, s));
    }

    @QueryMapping
    public Page<FeedbackResponseDTO> feedbackByPatient(
            @Argument Long patientId,
            @Argument Integer page,
            @Argument Integer size
    ) {
        int p = page != null ? page : 0;
        int s = size != null ? size : 10;
        return feedbackService.getByPatient(patientId, PageRequest.of(p, s));

    }

    @MutationMapping
    public FeedbackResponseDTO createFeedback(
            @Argument FeedbackRequestDTO input
    ) {
        return feedbackService.create(input);
    }

    @MutationMapping
    public FeedbackResponseDTO updateFeedback(
            @Argument Long id,
            @Argument FeedbackRequestDTO input
    ) {
        return feedbackService.update(id, input);
    }

    @MutationMapping
    public Boolean deleteFeedback(@Argument Long id) {
        feedbackService.delete(id);
        return true;
    }
}
