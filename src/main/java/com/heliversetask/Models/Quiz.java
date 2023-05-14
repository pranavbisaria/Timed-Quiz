package com.heliversetask.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String question;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Options> options;
    private Integer rightAnswer;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime endDate;
    private String status;
}
