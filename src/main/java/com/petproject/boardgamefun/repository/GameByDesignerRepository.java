package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.GameByDesigner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameByDesignerRepository extends JpaRepository<GameByDesigner, Integer> {
}