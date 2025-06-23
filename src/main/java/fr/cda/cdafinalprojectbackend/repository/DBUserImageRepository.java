package fr.cda.cdafinalprojectbackend.repository;

import fr.cda.cdafinalprojectbackend.document.DBUserImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DBUserImageRepository extends MongoRepository<DBUserImage, String> {
}
