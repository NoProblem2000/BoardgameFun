package com.petproject.boardgamefun.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;


public record UserDTO(Integer id, String name, String password, String role, String mail, String town, Double rating,
                      byte[] avatar, OffsetDateTime registrationDate) implements Serializable {
}
