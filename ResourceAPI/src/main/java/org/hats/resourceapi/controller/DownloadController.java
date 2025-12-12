package org.hats.resourceapi.controller;

import org.hats.resourceapi.exceptions.ResourceNotFoundException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/files")
public class DownloadController {

    /*
    * In the current version there'll be no memory buffering or streaming
    * In future versions this will be implemented
    * */

    @GetMapping("/front-client/{fileName}")
    public ResponseEntity<?> downloadFrontEndFile(@PathVariable String fileName) {
        return ResponseEntity.ok().body(new ByteArrayResource(fileName.getBytes()));
    }

    @GetMapping("/docker-images/{fileName}")
    public ResponseEntity<?> downloadDockerFile(@PathVariable String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("docker-images/" + fileName);

        if (inputStream == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] fileBytes;
        ByteArrayResource resource;
        try {
             fileBytes = inputStream.readAllBytes();
             resource = new ByteArrayResource(fileBytes);
        } catch (IOException e) {
            throw new ResourceNotFoundException();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(fileBytes.length)
                .body(resource);
    }
}
