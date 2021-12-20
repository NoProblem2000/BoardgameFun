package com.petproject.boardgamefun.model;

import javax.persistence.*;

@Table(name = "designers", indexes = {
        @Index(name = "designers_un", columnList = "name", unique = true)
})
@Entity
public class Designer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}