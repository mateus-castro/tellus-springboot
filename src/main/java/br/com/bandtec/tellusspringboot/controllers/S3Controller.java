package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.handlers.S3StorageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage")
public class S3Controller {

    private S3StorageHandler service;

    @Autowired
    S3Controller(S3StorageHandler amazonClient){
        this.service = amazonClient;
    }

    @CrossOrigin
    @GetMapping("/health")
    public ResponseEntity healthCheck(){
     return ResponseEntity.status(200).body("Running application");
    }

    @CrossOrigin
    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file){
        return ResponseEntity.status(201).body(service.uploadFile(file));
    }

    @CrossOrigin
    @GetMapping("/download/{fileName}")
    public ResponseEntity downloadFile(@PathVariable String fileName){
        byte[] data = service.downloadFile(fileName);
        if (data != null){
            ByteArrayResource resource = new ByteArrayResource(data);
            return ResponseEntity
                    .ok()
                    .contentLength(data.length)
                    .header("Content-type", "application/octet-stream")
                    .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        }
        return ResponseEntity.status(404).build();
    }
}
