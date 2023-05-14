package com.heliversetask.Service.Impl;

import com.heliversetask.Exceptions.ResourceNotFoundException;
import com.heliversetask.Models.Quiz;
import com.heliversetask.Models.QuizWinners;
import com.heliversetask.Models.Winners;
import com.heliversetask.Payloads.AnswerResponse;
import com.heliversetask.Payloads.ApiResponse;
import com.heliversetask.Repository.QuizRepo;
import com.heliversetask.Repository.QuizWinnersRepo;
import com.heliversetask.Service.AnswerAndWinnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class AnswerAndWinnerServiceImpl implements AnswerAndWinnerService {
    private final QuizRepo quizRepo;
    private final QuizWinnersRepo quizWinnersRepo;

    @Override
    public ResponseEntity<?> answerAQuiz(AnswerResponse answerResponse) {
        Quiz quiz = this.quizRepo.findById(answerResponse.getQuizId())
                .orElseThrow(()-> new ResourceNotFoundException("Quiz", "quizId: ", answerResponse.getQuizId()));

        if (!(0< answerResponse.getYourAnswer()) || !(answerResponse.getYourAnswer()<= quiz.getOptions().size())) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse("Please select a valid answer index!!", false));
        }
        //Checking if the quiz is active or not
        LocalDateTime now = LocalDateTime.now();
        if (!now.isAfter(quiz.getStartDate()) ||  !now.isBefore(quiz.getEndDate())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Ops!!.. Quiz is not active to answer", false));
        }

        Optional<QuizWinners> quizWinners = this.quizWinnersRepo.findById(answerResponse.getQuizId());
        QuizWinners quizWinner;
        if(quizWinners.isPresent()) {
            quizWinner = quizWinners.get();
        }
        else {
            quizWinner = new QuizWinners();
            quizWinner.setId(answerResponse.getQuizId());
        }
        if(Objects.equals(answerResponse.getYourAnswer(), quiz.getRightAnswer())) {
            Winners winner = new Winners(answerResponse.getName().trim());
            quizWinner.getWinnersList().add(winner);
        }
        this.quizWinnersRepo.save(quizWinner);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Congratulations your response has been recorded, check the winner list after the quiz is over", true));
    }

    @Override
    public ResponseEntity<?> getQuizWinners(Long quizID) {
        Quiz quiz = this.quizRepo.findById(quizID)
                .orElseThrow(()-> new ResourceNotFoundException("Quiz", "quizId: ", quizID));

        //Checking if the quiz is finished or not
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(quiz.getEndDate().plusMinutes(5))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Quiz result is not yet declared.!!", false));
        }
        Optional<QuizWinners> quizWinners = this.quizWinnersRepo.findById(quizID);
        QuizWinners quizWinner;
        if(quizWinners.isPresent()) {
            quizWinner = quizWinners.get();
            quizWinner.getWinnersList().sort(Comparator.comparing(Winners::getSubmittedAt));
            return ResponseEntity.status(HttpStatus.OK).body(quizWinner);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("The Quiz has no submissions", true));
        }

    }
}
