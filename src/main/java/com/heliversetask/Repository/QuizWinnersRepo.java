package com.heliversetask.Repository;

import com.heliversetask.Models.QuizWinners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizWinnersRepo extends JpaRepository<QuizWinners, Long> {
}
