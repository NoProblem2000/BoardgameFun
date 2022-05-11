package com.petproject.boardgamefun.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;

public record GameDTO(Integer id, String title, OffsetDateTime yearOfRelease, String annotation, String description,
                      byte[] picture, String playerAge, Integer playersMin, Integer playersMax, Integer timeToPlayMin,
                      Integer timeToPlayMax) implements Serializable {
}
