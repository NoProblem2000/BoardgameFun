package com.petproject.boardgamefun.model;

import javax.persistence.*;
import java.time.OffsetTime;

@Table(name = "user")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 1)
    private String name;

    @Lob
    @Column(name = "password", nullable = false)
    private String password;

    @Lob
    @Column(name = "role", nullable = false)
    private String role;

    @Lob
    @Column(name = "mail", nullable = false)
    private String mail;

    @Lob
    @Column(name = "town")
    private String town;

    @Column(name = "registration_date", nullable = false)
    private OffsetTime registrationDate;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "avatar")
    private byte[] avatar;

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public OffsetTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(OffsetTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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