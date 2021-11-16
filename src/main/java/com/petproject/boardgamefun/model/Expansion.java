package com.petproject.boardgamefun.model;

import javax.persistence.*;

@Table(name = "expansions")
@Entity
public class Expansion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "parent_game", nullable = false)
    private Game parentGame;

    @ManyToOne(optional = false)
    @JoinColumn(name = "daughter_game", nullable = false)
    private Game daughterGame;

    public Game getDaughterGame() {
        return daughterGame;
    }

    public void setDaughterGame(Game daughterGame) {
        this.daughterGame = daughterGame;
    }

    public Game getParentGame() {
        return parentGame;
    }

    public void setParentGame(Game parentGame) {
        this.parentGame = parentGame;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}