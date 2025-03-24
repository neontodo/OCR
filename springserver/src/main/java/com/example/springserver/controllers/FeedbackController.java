package com.example.springserver.controllers;

import com.example.springserver.dto.FeedbackDto;
import com.example.springserver.services.feedback.FeedbackServiceImp;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/feedback")
public class FeedbackController {
    private final FeedbackServiceImp feedbackService;

    @ApiOperation("Submitting Feedback")
    @PostMapping("/submit-feedback")
    public ResponseEntity<String> submitFeedback(@RequestBody FeedbackDto feedbackDto){
        feedbackService.submitFeedback(feedbackDto);
        return ResponseEntity.ok("Feedback Submitted Successfully");
    }
}
