package com.heliversetask.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Options {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer index;
    private String option;

    @PrePersist
    public void prePersist() {
        if (this.index == null) {
            int index = this.getQuiz().getOptions().indexOf(this);
            this.setIndex(index + 1);
        }
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
}
