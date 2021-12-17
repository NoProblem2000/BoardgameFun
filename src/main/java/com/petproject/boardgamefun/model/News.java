package com.petproject.boardgamefun.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Table(name = "news")
@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(optional = false)
    @JoinColumn(name = "\"user\"", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "game")
    private Game game;

    @Column(name = "publication_time", nullable = false)
    private OffsetDateTime publicationTime;

    public OffsetDateTime getPublicationTime() {
        return publicationTime;
    }

    public void setPublicationTime(OffsetDateTime publicationTime) {
        this.publicationTime = publicationTime;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}