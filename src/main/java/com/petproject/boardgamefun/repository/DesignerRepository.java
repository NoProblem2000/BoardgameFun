package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.dto.DesignersProjection;
import com.petproject.boardgamefun.model.Designer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DesignerRepository extends JpaRepository<Designer, Integer> {
    Designer findDesignerById(Integer id);
    Designer findDesignerByName(String name);
    @Query("select d.name as designer from Designer d " +
            "join GameByDesigner gbd on gbd.designer.id = d.id " +
            "where gbd.game.id = :gameId")
    List<DesignersProjection> findDesignersUsingGame(Integer gameId);
}