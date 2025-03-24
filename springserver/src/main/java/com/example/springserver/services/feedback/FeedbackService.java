package com.example.springserver.services.feedback;

import com.example.springserver.dto.FeedbackDto;

public interface FeedbackService {
    public void submitFeedback(FeedbackDto feedbackDto);
}
