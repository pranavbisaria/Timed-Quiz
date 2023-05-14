package com.heliversetask.Service.Impl;

import com.heliversetask.Models.Options;
import com.heliversetask.Models.Quiz;
import com.heliversetask.Payloads.QuizDto;
import com.heliversetask.Repository.QuizRepo;
import com.heliversetask.Service.QuizService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
            this.quizRepo.save(quiz);
        }
    }
}