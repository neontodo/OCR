package com.example.springserver.repositories;

import com.example.springserver.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    //List<Feedback> findFeedbackByUserId(Long userId);
}
