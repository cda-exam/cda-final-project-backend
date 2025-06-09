package fr.cda.cdafinalprojectbackend.dto.dog;

import java.util.Date;

public record DogDTO (
    String name,
    Date birthday,
    String description,
    String photo,
    String breed,
    String sex
) {
}
