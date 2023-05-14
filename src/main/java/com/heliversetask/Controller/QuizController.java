package com.heliversetask.Controller;

import com.heliversetask.Payloads.ApiResponse;
import com.heliversetask.Payloads.OptionsDto;
import com.heliversetask.Payloads.QuizDto;
import com.heliversetask.Service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @PostMapping()
    ResponseEntity<?> createQuiz(@Valid @RequestBody QuizDto quizDto){
        if (quizDto.getStartDate().compareTo(quizDto.getEndDate()) > 0) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse("Start date must not be greater than end date!!", false));
        }
        return ResponseEntity.status(OK).body(this.quizService.createNewQuiz(quizDto));
    }
}
