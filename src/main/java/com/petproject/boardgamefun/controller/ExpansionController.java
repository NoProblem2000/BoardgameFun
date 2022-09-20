package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.service.ExpansionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expansions")
public class ExpansionController {

    final ExpansionService expansionService;

    public ExpansionController(ExpansionService expansionService) {
        this.expansionService = expansionService;
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<List<GameDataDTO>> getExpansions(@PathVariable Integer gameId) {

        var gamesExpansions = expansionService.getExpansions(gameId);
        return new ResponseEntity<>(gamesExpansions, HttpStatus.OK);
    }

    @PostMapping("/{parentGameId}/{daughterGameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<GameDataDTO>> addExpansion(@PathVariable Integer parentGameId, @PathVariable Integer daughterGameId) {

        var gamesExpansions = expansionService.addExpansion(parentGameId, daughterGameId);
        return new ResponseEntity<>(gamesExpansions, HttpStatus.OK);
    }

    @DeleteMapping("/{parentGameId}/{daughterGameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<GameDataDTO>> deleteExpansion(@PathVariable Integer parentGameId, @PathVariable Integer daughterGameId) {

        var gamesExpansions = expansionService.deleteExpansion(parentGameId, daughterGameId);
        return new ResponseEntity<>(gamesExpansions, HttpStatus.OK);
    }
}
