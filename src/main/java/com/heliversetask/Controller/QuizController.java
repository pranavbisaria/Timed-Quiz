package com.heliversetask.Controller;

import com.heliversetask.Payloads.ApiResponse;
import com.heliversetask.Payloads.QuizDto;
import com.heliversetask.Service.QuizService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    //Create a quiz
    @PostMapping()
    @RateLimiter(name="quizCreateService", fallbackMethod = "rateLimiterFallback")
    ResponseEntity<?> createQuiz(@Valid @RequestBody QuizDto quizDto){
        if (quizDto.getStartDate().compareTo(quizDto.getEndDate()) > 0) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse("Start date must not be greater than end date!!", false));
        }
        if (!(0< quizDto.getRightAnswer()) || !(quizDto.getRightAnswer()<= quizDto.getOptions().size())) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse("Please select a valid answer index!!", false));
        }
        return ResponseEntity.status(OK).body(this.quizService.createNewQuiz(quizDto));
    }

    //Get all active quizzes
    @GetMapping("/active")
    @RateLimiter(name="default", fallbackMethod = "rateLimiterFallback")
    ResponseEntity<?> getAllActiveQuiz() {
        return ResponseEntity.status(OK).body(this.quizService.getActiveQuizzes());
    }

    //Get the quiz result if quiz ended
    @GetMapping("/{quizId}/result")
    @RateLimiter(name="default", fallbackMethod = "rateLimiterFallback")
    ResponseEntity<?> getQuizResult(@PathVariable("quizId") Long quizId){
        return this.quizService.getQuizResult(quizId);
    }

    //Get all the quiz
    @GetMapping("/all")
    @RateLimiter(name="default", fallbackMethod = "rateLimiterFallback")
    ResponseEntity<?> getAllQuizzes(){
        return ResponseEntity.status(OK).body(this.quizService.getAllQuizzes());
    }

    //Fallback method to handle rate-limiting response
    public ResponseEntity<?> rateLimiterFallback(Exception e){
        return ResponseEntity.status(TOO_MANY_REQUESTS).body(new ApiResponse("Limit Reached, further calls are not permitted, please try after some time!!", false));
    }
}
