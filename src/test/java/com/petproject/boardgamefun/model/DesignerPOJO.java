package com.petproject.boardgamefun.model;

import com.petproject.boardgamefun.dto.projection.DesignersProjection;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DesignerPOJO implements DesignersProjection {

    private String designer;

    @Override
    public String getDesigner() {
        return designer;
    }
}
