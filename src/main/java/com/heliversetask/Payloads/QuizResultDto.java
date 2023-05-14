package com.heliversetask.Payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heliversetask.Models.Options;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResultDto {
    private Long id;
    private String question;
    private Integer rightAnswer;
    private Options rightOption;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime endDate;
    private String status;
}
