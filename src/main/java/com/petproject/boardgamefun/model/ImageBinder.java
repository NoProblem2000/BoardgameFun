package com.petproject.boardgamefun.model;

import javax.persistence.*;

@Table(name = "image_binder")
@Entity
public class ImageBinder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "news")
    private News news;

    @ManyToOne
    @JoinColumn(name = "diary")
    private Diary diary;

    @ManyToOne(optional = false)
    @JoinColumn(name = "\"user\"", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "forum")
    private ForumMessage forum;

    @ManyToOne(optional = false)
    @JoinColumn(name = "image", nullable = false)
    private Image image;

    @ManyToOne
    @JoinColumn(name = "game")
    private Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public ForumMessage getForum() {
        return forum;
    }

    public void setForum(ForumMessage forum) {
        this.forum = forum;
    }

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

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}