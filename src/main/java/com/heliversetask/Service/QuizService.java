package com.heliversetask.Service;
import com.heliversetask.Models.Quiz;
import com.heliversetask.Payloads.QuizDto;

public interface QuizService {
    Quiz createNewQuiz(QuizDto quizDto);
}
