package com.imamachi.simplepolling.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "question_detail")
@Data
public class QuestionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false,updatable = false)
    private Integer questionDetailId;

    @Column(nullable = false)
    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "questionId", nullable = false)
    private Question question;
}
