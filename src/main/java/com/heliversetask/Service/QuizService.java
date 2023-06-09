package com.heliversetask.Service;
import com.heliversetask.Models.Quiz;
import com.heliversetask.Payloads.QuizDto;
import com.heliversetask.Payloads.ShowQuizDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuizService {
    Quiz createNewQuiz(QuizDto quizDto);

    List<ShowQuizDto> getActiveQuizzes();

    ResponseEntity<?> getQuizResult(Long quizId);

    List<ShowQuizDto> getAllQuizzes();
}
