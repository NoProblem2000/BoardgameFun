package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.DesignerDTO;
import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.dto.request.DesignerRequest;
import com.petproject.boardgamefun.service.DesignerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/designer")
public class DesignerController {

    private final DesignerService designerService;

    public DesignerController(DesignerService designerService) {
        this.designerService = designerService;
    }

    @GetMapping("")
    public ResponseEntity<List<DesignerDTO>> getDesigners() {
        var designers = designerService.getDesigners();
        return new ResponseEntity<>(designers, HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<DesignerDTO> getDesignerByName(@PathVariable String name) {
        var designer = designerService.getDesignerByName(name);
        return new ResponseEntity<>(designer, HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DesignerDTO>> addDesigner(@RequestBody DesignerRequest designerRequest) {
        var designers = designerService.addDesigner(designerRequest);
        return new ResponseEntity<>(designers, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DesignerDTO>> updateDesigner(@PathVariable Integer id, @RequestBody DesignerRequest designerRequest) {
        var designers = designerService.updateDesigner(id, designerRequest);
        return new ResponseEntity<>(designers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DesignerDTO>> deleteDesigner(@PathVariable Integer id) {
        var designers = designerService.deleteDesigner(id);
        return new ResponseEntity<>(designers, HttpStatus.OK);
    }


    @PostMapping("/{gameId}/{designerId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<GameDataDTO> addDesignerToGame(@PathVariable Integer gameId, @PathVariable Integer designerId) {
        var gameDTO = designerService.addDesignerToGame(gameId, designerId);
        return new ResponseEntity<>(gameDTO, HttpStatus.OK);
    }

    @DeleteMapping("{gameId}/{gameByDesignerId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<GameDataDTO> deleteDesignerFromGame(@PathVariable Integer gameId, @PathVariable Integer gameByDesignerId) {
        var gameDTO = designerService.deleteDesignerFromGame(gameId, gameByDesignerId);
        return new ResponseEntity<>(gameDTO, HttpStatus.OK);
    }


}
