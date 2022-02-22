package com.petproject.boardgamefun.security.services;

import com.petproject.boardgamefun.security.exception.RefreshTokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RefreshTokenService {

    @Value("${boardgamefun.app.refreshTokenExpirationMs}")
    private Long refreshTokenDurationMs;

    @Value("${boardgamefun.app.jwtRefreshSecret}")
    private String jwtRefreshSecret;

    public String createRefreshToken(String userName) {
        return Jwts.builder()
                .setSubject((userName))
                .setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + refreshTokenDurationMs))
                .signWith(SignatureAlgorithm.HS512, jwtRefreshSecret)
                .compact();
    }

    public boolean verifyExpiration(String authToken) {
        var token = authToken.substring(0, authToken.lastIndexOf('.') + 1);
        try {
            var expiration = ((Claims) Jwts.parser().parse(token).getBody()).getExpiration();
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenException(authToken, "refresh token was expired");
        }

        return true;
    }

}
