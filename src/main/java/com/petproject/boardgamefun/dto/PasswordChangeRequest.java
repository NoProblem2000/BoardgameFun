package com.petproject.boardgamefun.dto;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequest {
    private String password;
}
