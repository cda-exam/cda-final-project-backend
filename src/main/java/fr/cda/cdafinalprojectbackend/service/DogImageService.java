package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.document.DogImage;
import fr.cda.cdafinalprojectbackend.repository.DogImageRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("dogImageService")
public class DogImageService extends ImageService<DogImage> {
    public DogImageService(DogImageRepository dogImageRepository) {
        super(dogImageRepository, DogImage.class);
    }
}
