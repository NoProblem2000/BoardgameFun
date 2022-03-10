package com.petproject.boardgamefun.security.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private String name;
    private String password;
}
