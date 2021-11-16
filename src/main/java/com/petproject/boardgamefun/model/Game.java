package com.petproject.boardgamefun.model;

import javax.persistence.*;
import java.time.OffsetTime;

@Table(name = "game", indexes = {
        @Index(name = "game_un", columnList = "title", unique = true)
})
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "year_of_release", nullable = false)
    private OffsetTime yearOfRelease;

    @Lob
    @Column(name = "annotanion", nullable = false)
    private String annotanion;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Lob
    @Column(name = "players_number", nullable = false)
    private String playersNumber;

    @Lob
    @Column(name = "time_to_play")
    private String timeToPlay;

    @Lob
    @Column(name = "age")
    private String age;

    @Column(name = "picture")
    private byte[] picture;

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getTimeToPlay() {
        return timeToPlay;
    }

    public void setTimeToPlay(String timeToPlay) {
        this.timeToPlay = timeToPlay;
    }

    public String getPlayersNumber() {
        return playersNumber;
    }

    public void setPlayersNumber(String playersNumber) {
        this.playersNumber = playersNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnnotanion() {
        return annotanion;
    }

    public void setAnnotanion(String annotanion) {
        this.annotanion = annotanion;
    }

    public OffsetTime getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(OffsetTime yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
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