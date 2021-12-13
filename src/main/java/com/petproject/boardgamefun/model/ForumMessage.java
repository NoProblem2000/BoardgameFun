package com.petproject.boardgamefun.model;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

@Table(name = "forum_message")
@Entity
public class ForumMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "forum", nullable = false)
    private Forum forum;

    @Lob
    @Column(name = "comment", nullable = false)
    private String comment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "\"user\"", nullable = false)
    private User user;

    @Column(name = "\"time_message\"", nullable = false)
    private OffsetDateTime time;

    public OffsetDateTime getTime() {
        return time;
    }

    public void setTime(OffsetDateTime time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}