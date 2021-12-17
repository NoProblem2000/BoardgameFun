package com.petproject.boardgamefun.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.OffsetTime;

@Table(name = "news_comments")
@Entity
public class NewsComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "comment_time", nullable = false)
    private OffsetTime commentTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "\"user\"", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "news", nullable = false)
    private News news;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OffsetTime getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(OffsetTime commentTime) {
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