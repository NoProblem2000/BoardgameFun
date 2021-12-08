package com.petproject.boardgamefun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class UserEditRequest {
    private String name;
    private String role;
    private byte[] avatar;
}
