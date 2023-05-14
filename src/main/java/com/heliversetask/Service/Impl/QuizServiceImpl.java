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
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final QuizRepo quizRepo;
    private final ModelMapper modelMapper;

    //Service Logic for create a quiz API
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

    //Service Logic to get the active quizzes
    @Override
    public List<ShowQuizDto> getActiveQuizzes(){
        LocalDateTime now = LocalDateTime.now();
        List<Quiz> allActiveQuiz = this.quizRepo.findAllActiveQuizzes(now);
        return allActiveQuiz.stream()
                .map((activeQuiz)-> this.modelMapper.map(activeQuiz, ShowQuizDto.class))
                .toList();
    }

    //Service Logic to get the result of the quiz if finished
    @Override
    public ResponseEntity<?> getQuizResult(Long quizId){
        Quiz quiz = this.quizRepo.findById(quizId)
                .orElseThrow(()-> new ResourceNotFoundException("Quiz", "quizId: ", quizId));
        LocalDateTime now = LocalDateTime.now();
        //Checking if the quiz is finished or not
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

    //Service logic to get all the quiz
    @Override
    public List<ShowQuizDto> getAllQuizzes(){
        List<Quiz> allQuiz = this.quizRepo.findAll();
        return allQuiz
                .stream()
                .map((quiz)-> this.modelMapper.map(quiz, ShowQuizDto.class))
                .toList();
    }
}