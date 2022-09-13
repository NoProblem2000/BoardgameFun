package com.petproject.boardgamefun.dto.request;

import lombok.Data;

@Data
public class UserEditRequest {
    public String name;
    public String role;
    public byte[] avatar;
}
