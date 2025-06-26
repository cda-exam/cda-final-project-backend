package fr.cda.cdafinalprojectbackend.controller;

import fr.cda.cdafinalprojectbackend.document.DogImage;
import fr.cda.cdafinalprojectbackend.service.ImageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dog-images")
public class DogImageController extends ImageController<DogImage> {
    public DogImageController(@Qualifier("dogImageService") ImageService<DogImage> imageService) {
        super(imageService);
    }
}