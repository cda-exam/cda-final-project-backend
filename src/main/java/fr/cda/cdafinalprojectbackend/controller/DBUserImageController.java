package fr.cda.cdafinalprojectbackend.controller;

import fr.cda.cdafinalprojectbackend.document.DBUserImage;
import fr.cda.cdafinalprojectbackend.service.ImageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-images")
public class DBUserImageController extends ImageController<DBUserImage> {
    public DBUserImageController(@Qualifier("dbUserImageService") ImageService<DBUserImage> imageService) {
        super(imageService);
    }
}
