package com.example.springserver.services.feedback;

import com.example.springserver.dto.FeedbackDto;
import com.example.springserver.models.Feedback;
import com.example.springserver.models.mapper.FeedbackMapper;
import com.example.springserver.repositories.FeedbackRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FeedbackServiceImp implements FeedbackService{
    private final FeedbackRepository feedbackRepository;

    @Override
    public void submitFeedback(FeedbackDto feedbackDto) {
        Feedback feedback = FeedbackMapper.INSTANCE.feedbackDtoToFeedback(feedbackDto);
        feedbackRepository.save(feedback);
    }
}
