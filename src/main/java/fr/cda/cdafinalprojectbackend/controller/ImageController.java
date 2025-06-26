package fr.cda.cdafinalprojectbackend.controller;

import fr.cda.cdafinalprojectbackend.document.Image;
import fr.cda.cdafinalprojectbackend.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public abstract class ImageController <T extends Image> {
    private final ImageService<T> imageService;
    
    public ImageController(ImageService<T> imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("image") MultipartFile file) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String idImage = this.imageService.save(file);
        return ResponseEntity.ok(idImage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        T image = this.imageService.getImage(id);

        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header("Content-Type", image.getContentType())
                .body(image.getData());
    }
}
