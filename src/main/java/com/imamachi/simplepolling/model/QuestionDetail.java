package com.imamachi.simplepolling.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "question_detail")
@Data
@NoArgsConstructor
public class QuestionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false,updatable = false)
    private Integer questionDetailId;

    // 質問事項
    @Column(nullable = false)
    private String description;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "questionId", nullable = false)
    private Question question;

    public QuestionDetail(String description){
        this.description = description;
    }

    public QuestionDetail(Integer questionDetailId, String description){
        this.questionDetailId = questionDetailId;
        this.description = description;
    }
}
