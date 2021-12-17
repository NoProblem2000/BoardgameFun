package com.petproject.boardgamefun.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.OffsetTime;

@Table(name = "messenger")
@Entity
public class Messenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "is_red", nullable = false)
    private Boolean isRed = false;

    @Column(name = "message_time", nullable = false)
    private OffsetTime messageTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_to", nullable = false)
    private User userTo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_from", nullable = false)
    private User userFrom;

    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    public OffsetTime getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(OffsetTime messageTime) {
        this.messageTime = messageTime;
    }

    public Boolean getIsRed() {
        return isRed;
    }

    public void setIsRed(Boolean isRed) {
        this.isRed = isRed;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}