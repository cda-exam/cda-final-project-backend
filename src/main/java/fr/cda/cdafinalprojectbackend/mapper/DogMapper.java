package fr.cda.cdafinalprojectbackend.mapper;

import fr.cda.cdafinalprojectbackend.dto.dog.DogDTO;
import fr.cda.cdafinalprojectbackend.entity.Dog;
import fr.cda.cdafinalprojectbackend.entity.DBUser;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class DogMapper {

    /**
     * Convertit une entité Dog en DogDTO
     * @param dog l'entité Dog à convertir
     * @return le DogDTO correspondant
     */
    public DogDTO toDTO(Dog dog) {
        if (dog == null) {
            return null;
        }

        return new DogDTO(
                dog.getName(),
                dog.getBirthday(),
                dog.getDescription(),
                dog.getPhoto(),
                dog.getBreed(),
                dog.getSex()
        );
    }

    /**
     * Convertit un DogDTO en entité Dog
     * @param dogDTO le DTO à convertir
     * @param user l'utilisateur propriétaire du chien
     * @return l'entité Dog correspondante
     */
    public Dog toEntity(DogDTO dogDTO, DBUser user) {
        if (dogDTO == null) {
            return null;
        }

        Dog dog = new Dog();
        dog.setName(dogDTO.name());
        dog.setBirthday(dogDTO.birthday());
        dog.setDescription(dogDTO.description());
        dog.setPhoto(dogDTO.photo());
        dog.setBreed(dogDTO.breed());
        dog.setSex(dogDTO.sex());

        return dog;
    }

    /**
     * Convertit un DogDTO en entité Dog sans utilisateur (pour création)
     * @param dogDTO le DTO à convertir
     * @return l'entité Dog correspondante sans utilisateur
     */
    public Dog toEntity(DogDTO dogDTO) {
        if (dogDTO == null) {
            return null;
        }

        Dog dog = new Dog();
        dog.setName(dogDTO.name());
        dog.setBirthday(dogDTO.birthday());
        dog.setDescription(dogDTO.description());
        dog.setPhoto(dogDTO.photo());
        dog.setBreed(dogDTO.breed());
        dog.setSex(dogDTO.sex());
        // L'utilisateur sera défini séparément

        return dog;
    }

    /**
     * Met à jour une entité Dog existante avec les données d'un DogDTO
     * @param existingDog l'entité Dog existante à mettre à jour
     * @param dogDTO le DTO contenant les nouvelles données
     */
    public void updateEntity(Dog existingDog, DogDTO dogDTO) {
        if (existingDog == null || dogDTO == null) {
            return;
        }

        existingDog.setName(dogDTO.name());
        existingDog.setBirthday(dogDTO.birthday());
        existingDog.setDescription(dogDTO.description());
        existingDog.setPhoto(dogDTO.photo());
        existingDog.setBreed(dogDTO.breed());
        existingDog.setSex(dogDTO.sex());
        // Note: L'ID et l'utilisateur ne sont pas mis à jour
    }

    /**
     * Convertit une liste de Dog en liste de DogDTO
     * @param dogs la liste d'entités Dog à convertir
     * @return la liste de DogDTO correspondante
     */
    public List<DogDTO> toDTOList(List<Dog> dogs) {
        if (dogs == null) {
            return new ArrayList<>();
        }

        return dogs.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de DogDTO en liste de Dog
     * @param dogDTOs la liste de DTO à convertir
     * @param user l'utilisateur propriétaire des chiens
     * @return la liste d'entités Dog correspondante
     */
    public List<Dog> toEntityList(List<DogDTO> dogDTOs, DBUser user) {
        if (dogDTOs == null) {
            return new ArrayList<>();
        }

        return dogDTOs.stream()
                .map(dto -> toEntity(dto, user))
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de DogDTO en liste de Dog sans utilisateur
     * @param dogDTOs la liste de DTO à convertir
     * @return la liste d'entités Dog correspondante
     */
    public List<Dog> toEntityList(List<DogDTO> dogDTOs) {
        if (dogDTOs == null) {
            return new ArrayList<>();
        }

        return dogDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}