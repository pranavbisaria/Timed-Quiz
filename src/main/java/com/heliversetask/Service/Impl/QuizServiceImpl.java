package com.heliversetask.Service.Impl;

import com.google.common.cache.Cache;
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
    private final Cache<String, Object> quizResultCache;

    //Service Logic for create a quiz API
    @Override
    public Quiz createNewQuiz(QuizDto quizDto){
        Quiz quiz = this.modelMapper.map(quizDto, Quiz.class);
        quiz.setStatus("inactive");
        quiz.setQuestion(quiz.getQuestion().trim());
        for (Options option : quiz.getOptions()) {
            option.setOption(option.getOption().trim());
            option.setQuiz(quiz);
        }
        this.quizRepo.save(quiz);

        // Invalidate the cache since new data is added
        this.quizResultCache.invalidateAll();

        return quiz;
    }

    //Service Logic to get the active quizzes
    @Override
    public List<ShowQuizDto> getActiveQuizzes(){
        LocalDateTime now = LocalDateTime.now();

        // Check if result exists in cache
        List<ShowQuizDto> quizResultDtoList = (List<ShowQuizDto>) this.quizResultCache.getIfPresent("activeQuizzes");
        if (quizResultDtoList != null) {
            return quizResultDtoList;
        }

        List<Quiz> allActiveQuiz = this.quizRepo.findAllActiveQuizzes(now);
        List<ShowQuizDto> showQuizDtoList = allActiveQuiz.stream()
                .map((activeQuiz)-> this.modelMapper.map(activeQuiz, ShowQuizDto.class))
                .toList();

        // Put result in cache
        this.quizResultCache.put("activeQuizzes", showQuizDtoList);
        return showQuizDtoList;
    }

    //Service Logic to get the result of the quiz if finished
    @Override
    public ResponseEntity<?> getQuizResult(Long quizId){
        QuizResultDto cachedResult = (QuizResultDto)quizResultCache.getIfPresent(quizId.toString());
        if (cachedResult != null && cachedResult.getStatus().equals("finished")) {
            return ResponseEntity.status(HttpStatus.OK).body(cachedResult);
        }

        Quiz quiz = this.quizRepo.findById(quizId)
                .orElseThrow(()-> new ResourceNotFoundException("Quiz", "quizId: ", quizId));

        //Checking if the quiz is finished or not
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(quiz.getEndDate().plusMinutes(5))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Quiz result is not yet declared.!!", false));
        }

        QuizResultDto quizResult = new QuizResultDto(
                quiz.getId(),
                quiz.getQuestion(),
                quiz.getRightAnswer(),
                quiz.getOptions().get(quiz.getRightAnswer() - 1),
                quiz.getStartDate(),
                quiz.getEndDate(),
                quiz.getStatus()
        );

        quizResultCache.put(quizId.toString(), quizResult);
        return ResponseEntity.status(HttpStatus.OK).body(quizResult);
    }

    //Service logic to get all the quiz
    @Override
    public List<ShowQuizDto> getAllQuizzes(){
        // Check if result exists in cache
        List<ShowQuizDto> quizResultDtoList = (List<ShowQuizDto>) this.quizResultCache.getIfPresent("allQuizzes");
        if (quizResultDtoList != null) {
            return quizResultDtoList;
        }

        List<Quiz> allQuiz = this.quizRepo.findAll();
        List<ShowQuizDto> showQuizDtoList = allQuiz
                .stream()
                .map((quiz)-> this.modelMapper.map(quiz, ShowQuizDto.class))
                .toList();

        // Put result in cache
        this.quizResultCache.put("allQuizzes", showQuizDtoList);
        return showQuizDtoList;
    }
}