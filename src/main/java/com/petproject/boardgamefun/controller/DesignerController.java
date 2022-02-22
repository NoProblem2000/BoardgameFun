package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.DesignerDTO;
import com.petproject.boardgamefun.dto.request.DesignerRequest;
import com.petproject.boardgamefun.model.Designer;
import com.petproject.boardgamefun.repository.DesignerRepository;
import com.petproject.boardgamefun.service.DesignerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/designer")
public class DesignerController {

    private final DesignerRepository designerRepository;
    private final DesignerService designerService;

    public DesignerController(DesignerRepository designerRepository, DesignerService designerService) {
        this.designerRepository = designerRepository;
        this.designerService = designerService;
    }

    @Transactional
    @GetMapping("")
    public ResponseEntity<List<DesignerDTO>> getDesigners(){
        var designers =  designerService.entitiesToDesignerDTO(designerRepository.findAll());

        return new ResponseEntity<>(designers, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/{name}")
    public ResponseEntity<DesignerDTO> getDesignerByName(@PathVariable String name){
        var designer = designerService.entityToDesignerDTO(designerRepository.findDesignerByName(name));

        return new ResponseEntity<>(designer, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DesignerDTO>> addDesigner(@RequestBody DesignerRequest designerRequest){
        var designer = new Designer();
        designer.setName(designerRequest.getName());
        designerRepository.save(designer);

        var designers = designerService.entitiesToDesignerDTO(designerRepository.findAll());

        return new ResponseEntity<>(designers, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/update/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DesignerDTO>> updateDesigner(@PathVariable Integer id, @RequestBody DesignerRequest designerRequest){
        var designer = designerRepository.findDesignerById(id);

        designer.setName(designerRequest.getName());
        designerRepository.save(designer);

        var designers = designerService.entitiesToDesignerDTO(designerRepository.findAll());

        return new ResponseEntity<>(designers, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DesignerDTO>> deleteDesigner(@PathVariable Integer id){

        var designer = designerRepository.findDesignerById(id);

        designerRepository.delete(designer);

        var designers = designerService.entitiesToDesignerDTO(designerRepository.findAll());

        return new ResponseEntity<>(designers, HttpStatus.OK);
    }



}
