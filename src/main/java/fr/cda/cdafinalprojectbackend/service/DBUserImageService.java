package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.document.DBUserImage;
import fr.cda.cdafinalprojectbackend.repository.DBUserImageRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("dbUserImageService")
public class DBUserImageService extends ImageService<DBUserImage> {
    public DBUserImageService(DBUserImageRepository dbUserImageRepository) {
        super(dbUserImageRepository, DBUserImage.class);
    }
}