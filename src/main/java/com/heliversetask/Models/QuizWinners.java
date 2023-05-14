package com.heliversetask.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizWinners {
    @Id
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Winners> winnersList = new ArrayList<>(0);
}
