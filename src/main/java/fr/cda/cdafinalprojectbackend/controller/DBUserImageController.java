package fr.cda.cdafinalprojectbackend.controller;

import fr.cda.cdafinalprojectbackend.document.DBUserImage;
import fr.cda.cdafinalprojectbackend.service.DBUserImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-images")
public class DBUserImageController {
    private final DBUserImageService dbUserImageService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("image") MultipartFile file) throws IOException {
        String idImage = this.dbUserImageService.save(file);
        return ResponseEntity.ok(idImage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        DBUserImage image = this.dbUserImageService.getImage(id);

        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header("Content-Type", image.getContentType())
                .body(image.getData());
    }
}
