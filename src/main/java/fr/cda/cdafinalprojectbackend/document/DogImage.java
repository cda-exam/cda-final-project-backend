package fr.cda.cdafinalprojectbackend.document;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dog_images")
public class DogImage extends Image {
}