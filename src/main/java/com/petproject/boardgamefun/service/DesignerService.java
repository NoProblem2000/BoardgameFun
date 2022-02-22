package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.DesignerDTO;
import com.petproject.boardgamefun.model.Designer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DesignerService {

    public List<DesignerDTO> entitiesToDesignerDTO(List<Designer> designers) {
        List<DesignerDTO> designersDTO = new ArrayList<>();

        for (var item :
                designers) {
            designersDTO.add(new DesignerDTO(item.getId(), item.getName()));
        }
        return designersDTO;
    }

    public DesignerDTO entityToDesignerDTO(Designer designer){
        return new DesignerDTO(designer.getId(), designer.getName());
    }
}
