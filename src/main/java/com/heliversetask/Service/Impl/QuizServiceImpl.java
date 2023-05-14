package com.heliversetask.Service.Impl;

import com.heliversetask.Exceptions.ResourceNotFoundException;
import com.heliversetask.Models.Options;
import com.heliversetask.Models.Quiz;
import com.heliversetask.Payloads.ApiResponse;
import com.heliversetask.Payloads.QuizDto;
import com.heliversetask.Payloads.QuizResultDto;
import com.heliversetask.Payloads.ShowQuizDto;
import com.heliversetask.Repository.QuizRepo;
import com.heliversetask.Service.QuizService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final QuizRepo quizRepo;
    private final ModelMapper modelMapper;

    @Override
    public Quiz createNewQuiz(QuizDto quizDto){
        Quiz quiz = this.modelMapper.map(quizDto, Quiz.class);
        quiz.setStatus("inactive");
        for (Options option : quiz.getOptions()) {
            option.setQuiz(quiz);
        }
        this.quizRepo.save(quiz);
        return quiz;
    }

    @Override
    public List<ShowQuizDto> getActiveQuizzes(){
        LocalDateTime now = LocalDateTime.now();
        List<Quiz> allActiveQuiz = this.quizRepo.findAllActiveQuizzes(now);
        return allActiveQuiz.stream()
                .map((activeQuiz)-> this.modelMapper.map(activeQuiz, ShowQuizDto.class))
                .toList();
    }

    @Override
    public ResponseEntity<?> getQuizResult(Long quizId){
        Quiz quiz = this.quizRepo.findById(quizId)
                .orElseThrow(()-> new ResourceNotFoundException("Quiz", "quizId: ", quizId));
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(quiz.getEndDate().plusMinutes(5))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Quiz result is not yet declared.!!", false));
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new QuizResultDto(
                        quiz.getId(),
                        quiz.getQuestion(),
                        quiz.getRightAnswer(),
                        quiz.getOptions().get(quiz.getRightAnswer()-1),
                        quiz.getStartDate(),
                        quiz.getEndDate()
                )
        );
    }

    // ------------------------------------------- Scheduler -----------------------------------------------------

    //This run every minute
    @Scheduled(cron = "0 * * * * *")
    public void updateQuizStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<Quiz> quizzesToUpdate = this.quizRepo.findAllQuizzesToUpdateStatus(now);
        for (Quiz quiz : quizzesToUpdate) {
            if (quiz.getStartDate().isAfter(now)) {
                quiz.setStatus("inactive");
            } else if (quiz.getEndDate().isBefore(now)) {
                quiz.setStatus("finished");
            } else {
                quiz.setStatus("active");
            }
            this.quizRepo.saveAndFlush(quiz);
        }
    }
}