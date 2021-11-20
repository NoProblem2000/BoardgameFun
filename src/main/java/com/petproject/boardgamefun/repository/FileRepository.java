package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Integer> {
}