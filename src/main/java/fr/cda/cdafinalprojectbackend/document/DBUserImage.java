package fr.cda.cdafinalprojectbackend.document;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_images")
public class DBUserImage extends Image {
}