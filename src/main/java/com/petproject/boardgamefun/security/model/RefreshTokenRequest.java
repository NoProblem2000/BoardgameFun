package com.petproject.boardgamefun.security.model;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String userName;
    private String refreshToken;
}
