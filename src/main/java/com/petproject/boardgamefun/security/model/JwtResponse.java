package com.petproject.boardgamefun.security.model;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private final String type = "Bearer";
    private Integer id;
    private String userName;
    private String email;
    private String refreshToken;

    public JwtResponse(String jwt, Integer id, String username, String email, String refreshToken) {
        this.token = jwt;
        this.id = id;
        this.userName = username;
        this.email = email;
        this.refreshToken = refreshToken;
    }
}
