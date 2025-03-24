package com.example.springserver.dto;

import com.example.springserver.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackDto {
    private String feedbackText;
    private String feedbackCategory;
}
