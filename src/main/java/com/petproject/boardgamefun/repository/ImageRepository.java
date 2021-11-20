package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {
}