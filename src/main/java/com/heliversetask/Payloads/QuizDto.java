package com.heliversetask.Payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heliversetask.Models.Options;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizDto {
    @NotBlank(message = "Question can bot be blank")
    private String question;
    @NotNull(message = "Options can bot be blank")
    private List<Options> options;
    @NotNull(message = "Answer can not be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Integer rightAnswer;
    @NotNull(message = "Please Enter a valid Start-Date")
    @FutureOrPresent(message = "Start date must be in the present or future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startDate;
    @NotNull(message = "Please Enter a valid End-Date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime endDate;
}
