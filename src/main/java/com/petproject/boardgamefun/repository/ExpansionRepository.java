package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.Expansion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpansionRepository extends JpaRepository<Expansion, Integer> {
}