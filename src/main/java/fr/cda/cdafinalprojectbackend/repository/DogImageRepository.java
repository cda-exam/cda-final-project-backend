package fr.cda.cdafinalprojectbackend.repository;

import fr.cda.cdafinalprojectbackend.document.DogImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DogImageRepository extends MongoRepository<DogImage, String> {
}
