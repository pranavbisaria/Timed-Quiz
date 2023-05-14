package com.heliversetask.Scheduler;

import com.heliversetask.Models.Quiz;
import com.heliversetask.Repository.QuizRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class QuizStatusScheduler {
    private final QuizRepo quizRepo;

    //Cron Scheduler that runs after every 1 minute, to save the computation I have added a query to update only the active quizzes or the past quizzes
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
