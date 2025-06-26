package fr.cda.cdafinalprojectbackend.document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Image {
    @Id
    private String id;
    private String fileName;
    private String contentType;
    private byte[] data;
}
