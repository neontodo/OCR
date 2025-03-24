package com.example.springserver.models.mapper;

import com.example.springserver.dto.FeedbackDto;
import com.example.springserver.models.Feedback;
import com.example.springserver.repositories.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    FeedbackMapper INSTANCE = Mappers.getMapper(FeedbackMapper.class);

    default Feedback feedbackDtoToFeedback(FeedbackDto feedbackDto){
        Feedback feedback = Feedback.builder().
                feedbackText(feedbackDto.getFeedbackText()).
                feedbackCategory(feedbackDto.getFeedbackCategory()).
                build();
        return feedback;
    }
}
