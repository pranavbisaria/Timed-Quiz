package com.heliversetask.Payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heliversetask.Models.Options;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowQuizDto {
    private Long id;

    private String question;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ShowOptionDto> options;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime endDate;

    private String status;
}
