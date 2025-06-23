package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.document.DBUserImage;
import fr.cda.cdafinalprojectbackend.entity.DBUser;
import fr.cda.cdafinalprojectbackend.repository.DBUserImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DBUserImageService {
    private final DBUserImageRepository dbUserImageRepository;

    public String save(MultipartFile file) throws IOException {
        DBUserImage image = new DBUserImage();
        image.setFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setData(file.getBytes());
        DBUserImage savedImage = this.dbUserImageRepository.save(image);
        return savedImage.getId();
    }

    public DBUserImage getImage(String id) {
        return this.dbUserImageRepository.findById(id).orElseThrow(
                () -> new RuntimeException(
                        "Aucune image pour l'id : " + id
                )
        );
    }
}
