package com.petproject.boardgamefun.model;

import javax.persistence.*;

@Table(name = "user_ratings")
@Entity
public class UserRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "comment")
    private String comment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "evaluating_user", nullable = false)
    private User evaluatingUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "evaluated_user", nullable = false)
    private User evaluatedUser;

    @Column(name = "rating", nullable = false)
    private Double rating;

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public User getEvaluatedUser() {
        return evaluatedUser;
    }

    public void setEvaluatedUser(User evaluatedUser) {
        this.evaluatedUser = evaluatedUser;
    }

    public User getEvaluatingUser() {
        return evaluatingUser;
    }

    public void setEvaluatingUser(User evaluatingUser) {
        this.evaluatingUser = evaluatingUser;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}