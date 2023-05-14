package com.heliversetask.Controller;

import com.heliversetask.Payloads.AnswerResponse;
import com.heliversetask.Service.AnswerAndWinnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quizzes")
public class AnswerAndWinnerController {
    private final AnswerAndWinnerService answerAndWinnerService;

    //Answer a quiz
    @PostMapping("/answer")
    public ResponseEntity<?> answerAQuiz(@Valid @RequestBody AnswerResponse answerResponse){
        return this.answerAndWinnerService.answerAQuiz(answerResponse);
    }

    //Get all winners
    @GetMapping("/winners/{quizId}")
    public ResponseEntity<?> getAllWinners(@PathVariable("quizId") Long quizId){
        return this.answerAndWinnerService.getQuizWinners(quizId);
    }
}
