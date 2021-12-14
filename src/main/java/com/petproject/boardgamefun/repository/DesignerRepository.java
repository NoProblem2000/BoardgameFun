package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.Designer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignerRepository extends JpaRepository<Designer, Integer> {
    Designer findDesignerById(Integer id);
    Designer findDesignerByName(String name);
}