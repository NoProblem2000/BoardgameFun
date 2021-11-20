package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.model.Image;
import com.petproject.boardgamefun.repository.ImageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageController {
    final ImageRepository imageRepository;

    public ImageController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping()
    ResponseEntity<List<Image>> getImages() {
        var images = imageRepository.findAll();
        return new ResponseEntity<>(images, HttpStatus.OK);

    }
}
