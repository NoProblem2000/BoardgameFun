package com.petproject.boardgamefun.model;

import javax.persistence.*;

@Table(name = "same_game")
@Entity
public class SameGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reference_game", nullable = false)
    private Game referenceGame;

    @ManyToOne(optional = false)
    @JoinColumn(name = "source_game", nullable = false)
    private Game sourceGame;

    public Game getSourceGame() {
        return sourceGame;
    }

    public void setSourceGame(Game sourceGame) {
        this.sourceGame = sourceGame;
    }

    public Game getReferenceGame() {
        return referenceGame;
    }

    public void setReferenceGame(Game referenceGame) {
        this.referenceGame = referenceGame;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}