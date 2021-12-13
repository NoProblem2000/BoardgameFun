package com.petproject.boardgamefun.dto;

import lombok.Data;

@Data
public class UserEditRequest {
    private String name;
    private String role;
    private byte[] avatar;
}
