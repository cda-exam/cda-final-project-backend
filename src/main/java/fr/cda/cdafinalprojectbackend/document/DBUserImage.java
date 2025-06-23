package fr.cda.cdafinalprojectbackend.document;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_images")
@Getter
@Setter
public class DBUserImage {
    @Id
    private String id;
    private String fileName;
    private String contentType;
    private byte[] data;
}
