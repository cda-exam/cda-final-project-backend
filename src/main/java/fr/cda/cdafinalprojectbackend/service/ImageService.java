package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.document.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class ImageService <T extends Image> {
    private MongoRepository<T, String> repository;
    private final Class<T> imageClass;

    public ImageService(MongoRepository<T, String> repository, Class<T> imageClass) {
        this.repository = repository;
        this.imageClass = imageClass;
    }
    
    public String save(MultipartFile file) throws
            IOException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException
    {
        Constructor<T> constructor = imageClass.getDeclaredConstructor();
        T image = constructor.newInstance();
        image.setFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setData(file.getBytes());
        T savedImage = this.repository.save(image);
        return savedImage.getId();
    }

    public T getImage(String id) {
        return this.repository.findById(id).orElseThrow(
                () -> new RuntimeException(
                        "Aucune image pour l'id : " + id
                )
        );
    }
}
