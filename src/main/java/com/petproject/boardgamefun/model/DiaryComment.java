package com.petproject.boardgamefun.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.OffsetDateTime;


@Table(name = "diary_comment")
@Entity
public class DiaryComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "comment_time", nullable = false)
    private OffsetDateTime commentTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "diary", nullable = false)
    private Diary diary;

    @ManyToOne(optional = false)
    @JoinColumn(name = "\"user\"", nullable = false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Diary getDiary() {
        return diary;
    }

    public void setDiary(Diary diary) {
        this.diary = diary;
    }

    public OffsetDateTime getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(OffsetDateTime commentTime) {
        this.commentTime = commentTime;
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