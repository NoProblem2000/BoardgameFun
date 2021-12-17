package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.request.DesignerRequest;
import com.petproject.boardgamefun.model.Designer;
import com.petproject.boardgamefun.repository.DesignerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/designer")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DesignerController {

    private final DesignerRepository designerRepository;

    public DesignerController(DesignerRepository designerRepository) {
        this.designerRepository = designerRepository;
    }

    @Transactional
    @GetMapping("")
    public ResponseEntity<List<Designer>> getDesigners(){
        var designers = designerRepository.findAll();

        return new ResponseEntity<>(designers, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/{name}")
    public ResponseEntity<Designer> getDesignerByName(@PathVariable String name){
        var designers = designerRepository.findDesignerByName(name);

        return new ResponseEntity<>(designers, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<List<Designer>> addDesigner(@RequestBody DesignerRequest designerRequest){
        var designer = new Designer();
        designer.setName(designerRequest.getName());
        designerRepository.save(designer);

        var designers = designerRepository.findAll();

        return new ResponseEntity<>(designers, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/update/{id}")
    public ResponseEntity<List<Designer>> updateDesigner(@PathVariable Integer id, @RequestBody DesignerRequest designerRequest){
        var designer = designerRepository.findDesignerById(id);

        designer.setName(designerRequest.getName());
        designerRepository.save(designer);

        var designers = designerRepository.findAll();

        return new ResponseEntity<>(designers, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<List<Designer>> deleteDesigner(@PathVariable Integer id){

        var designer = designerRepository.findDesignerById(id);

        designerRepository.delete(designer);

        var designers = designerRepository.findAll();

        return new ResponseEntity<>(designers, HttpStatus.OK);
    }



}
