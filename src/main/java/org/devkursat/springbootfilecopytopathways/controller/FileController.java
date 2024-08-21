package org.devkursat.springbootfilecopytopathways.controller;

import lombok.AllArgsConstructor;
import org.devkursat.springbootfilecopytopathways.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/file")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("")
    public ResponseEntity<String> getHello() {
        return ResponseEntity.ok().body("Hello World");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok().body(fileService.saveFile_IoAPI(file));
    }
}
