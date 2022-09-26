package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.DesignerDTO;
import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.dto.request.DesignerRequest;
import com.petproject.boardgamefun.service.DesignerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation
            (value = "Get all designers", notes = "Return designer list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Designers successfully retrieved")
    })
    @GetMapping("")
    public ResponseEntity<List<DesignerDTO>> getDesigners() {
        var designers = designerService.getDesigners();
        return new ResponseEntity<>(designers, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Get designer by name", notes = "Return designer model")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Designer successfully retrieved"),
            @ApiResponse(code = 404, message = "Designer with that name does not exist")
    })
    @GetMapping("/{name}")
    public ResponseEntity<DesignerDTO> getDesignerByName(@PathVariable String name) {
        var designer = designerService.getDesignerByName(name);
        return new ResponseEntity<>(designer, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Add designer", notes = "Return designer list with new model")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Designer successfully retrieved"),
            @ApiResponse(code = 401, message = "You are not authorized")
    })
    @PostMapping("")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DesignerDTO>> addDesigner(@RequestBody DesignerRequest designerRequest) {
        var designers = designerService.addDesigner(designerRequest);
        return new ResponseEntity<>(designers, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Update designer data", notes = "Return designer list with updated model")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Designer successfully updated"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Designer not found")
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DesignerDTO>> updateDesigner(@PathVariable Integer id, @RequestBody DesignerRequest designerRequest) {
        var designers = designerService.updateDesigner(id, designerRequest);
        return new ResponseEntity<>(designers, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Delete designer", notes = "Return designer list without deleted model")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Designer successfully deleted"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Designer not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DesignerDTO>> deleteDesigner(@PathVariable Integer id) {
        var designers = designerService.deleteDesigner(id);
        return new ResponseEntity<>(designers, HttpStatus.OK);
    }


    @ApiOperation
            (value = "Add designer to game", notes = "Return game with new data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Designer successfully added to game"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Designer or Game not found")
    })
    @PostMapping("/{gameId}/{designerId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<GameDataDTO> addDesignerToGame(@PathVariable Integer gameId, @PathVariable Integer designerId) {
        var gameDTO = designerService.addDesignerToGame(gameId, designerId);
        return new ResponseEntity<>(gameDTO, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Delete designer from game", notes = "Return game with new data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Designer successfully deleted from game"),
            @ApiResponse(code = 401, message = "You are not authorized")
    })
    @DeleteMapping("{gameId}/{gameByDesignerId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<GameDataDTO> deleteDesignerFromGame(@PathVariable Integer gameId, @PathVariable Integer gameByDesignerId) {
        var gameDTO = designerService.deleteDesignerFromGame(gameId, gameByDesignerId);
        return new ResponseEntity<>(gameDTO, HttpStatus.OK);
    }


}
