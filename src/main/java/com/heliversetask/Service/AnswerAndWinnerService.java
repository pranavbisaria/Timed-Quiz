package com.heliversetask.Service;

import com.heliversetask.Models.Quiz;
import com.heliversetask.Payloads.AnswerResponse;
import org.springframework.http.ResponseEntity;

public interface AnswerAndWinnerService {
    ResponseEntity<?> answerAQuiz(AnswerResponse answerResponse);

    ResponseEntity<?> getQuizWinners(Long quizID);
}
