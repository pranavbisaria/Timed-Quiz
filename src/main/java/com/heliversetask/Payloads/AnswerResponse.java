package com.heliversetask.Payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponse {
    @NotBlank(message = "Name field can not be blank")
    private String name;
    @NotNull(message = "Enter a valid Quiz Id")
    private Long quizId;
    @NotNull(message = "Enter a valid answer")
    private Integer yourAnswer;
}
