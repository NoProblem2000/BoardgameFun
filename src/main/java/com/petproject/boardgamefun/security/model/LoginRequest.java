package com.petproject.boardgamefun.security.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginRequest {

    private String name;
    private String password;
}
