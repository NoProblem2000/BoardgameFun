package com.petproject.boardgamefun.model;

import javax.persistence.*;

@Table(name = "game_by_designers")
@Entity
public class GameByDesigner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "designer", nullable = false)
    private Designer designer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game", nullable = false)
    private Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Designer getDesigner() {
        return designer;
    }

    public void setDesigner(Designer designer) {
        this.designer = designer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}