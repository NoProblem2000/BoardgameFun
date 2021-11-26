package com.petproject.boardgamefun.security.model;

public class JwtResponse {
    private final String token;
    private final String userName;
    private final String email;

    public JwtResponse(String token, String userName, String email){
        this.token = token;
        this.userName = userName;
        this.email = email;
    }
}
