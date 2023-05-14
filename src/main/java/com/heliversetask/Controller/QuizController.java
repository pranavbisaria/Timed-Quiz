package com.heliversetask.Controller;

import com.heliversetask.Payloads.ApiResponse;
import com.heliversetask.Payloads.QuizDto;
import com.heliversetask.Service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    //Create a quiz
    @PostMapping()
    ResponseEntity<?> createQuiz(@Valid @RequestBody QuizDto quizDto){
        if (quizDto.getStartDate().compareTo(quizDto.getEndDate()) > 0) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse("Start date must not be greater than end date!!", false));
        }
        return ResponseEntity.status(OK).body(this.quizService.createNewQuiz(quizDto));
    }

    //Get all active quizzes
    @GetMapping("/active")
    ResponseEntity<?> getAllActiveQuiz() {
        return ResponseEntity.status(OK).body(this.quizService.getActiveQuizzes());
    }

    //Get the quiz result if quiz ended
    @GetMapping("/{quizId}/result")
    ResponseEntity<?> getQuizResult(@PathVariable("quizId") Long quizId){
        return this.quizService.getQuizResult(quizId);
    }

    //Get all the quiz
    @GetMapping("/all")
    ResponseEntity<?> getAllQuizzes(){
        return ResponseEntity.status(OK).body(this.quizService.getAllQuizzes());
    }
}
