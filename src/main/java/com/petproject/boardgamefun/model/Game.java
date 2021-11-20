package com.petproject.boardgamefun.model;

import org.hibernate.annotations.Type;

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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "year_of_release", nullable = false)
    private OffsetTime yearOfRelease;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "annotation", nullable = false)
    private String annotation;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "player_age")
    private String playerAge;

    @Column(name = "players_min", nullable = false)
    private Integer playersMin;

    @Column(name = "players_max", nullable = false)
    private Integer playersMax;

    @Column(name = "time_to_play_min", nullable = false)
    private Integer timeToPlayMin;

    @Column(name = "time_to_play_max", nullable = false)
    private Integer timeToPlayMax;

    public Integer getTimeToPlayMax() {
        return timeToPlayMax;
    }

    public void setTimeToPlayMax(Integer timeToPlayMax) {
        this.timeToPlayMax = timeToPlayMax;
    }

    public Integer getTimeToPlayMin() {
        return timeToPlayMin;
    }

    public void setTimeToPlayMin(Integer timeToPlayMin) {
        this.timeToPlayMin = timeToPlayMin;
    }

    public Integer getPlayersMax() {
        return playersMax;
    }

    public void setPlayersMax(Integer playersMax) {
        this.playersMax = playersMax;
    }

    public Integer getPlayersMin() {
        return playersMin;
    }

    public void setPlayersMin(Integer playersMin) {
        this.playersMin = playersMin;
    }

    public String getPlayerAge() {
        return playerAge;
    }

    public void setPlayerAge(String playerAge) {
        this.playerAge = playerAge;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
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