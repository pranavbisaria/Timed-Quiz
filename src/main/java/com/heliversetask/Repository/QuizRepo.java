package com.heliversetask.Repository;

import com.heliversetask.Models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface QuizRepo extends JpaRepository<Quiz, Long> {
    @Query("SELECT q FROM Quiz q WHERE q.status <> 'finished' AND (q.startDate <= :now OR q.endDate <= :now)")
    List<Quiz> findAllQuizzesToUpdateStatus(@Param("now") LocalDateTime now);

    @Query("SELECT q FROM Quiz q WHERE (q.startDate <= :now AND q.endDate >= :now And q.status = 'active')")
    List<Quiz> findAllActiveQuizzes(@Param("now") LocalDateTime now);
}
