package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.configuration.security.RoleEnum;
import fr.cda.cdafinalprojectbackend.dto.dog.DogDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserCreateDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserUpdateDTO;
import fr.cda.cdafinalprojectbackend.entity.DBUser;
import fr.cda.cdafinalprojectbackend.entity.Dog;
import fr.cda.cdafinalprojectbackend.entity.Role;
import fr.cda.cdafinalprojectbackend.entity.Validation;
import fr.cda.cdafinalprojectbackend.exception.DogNotFoundException;
import fr.cda.cdafinalprojectbackend.exception.UserAlreadyExistsException;
import fr.cda.cdafinalprojectbackend.exception.UserNotFoundException;
import fr.cda.cdafinalprojectbackend.mapper.DogMapper;
import fr.cda.cdafinalprojectbackend.repository.DogRepository;
import fr.cda.cdafinalprojectbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DogServiceImpl implements DogService {

    private final DogRepository dogRepository;
    private final DogMapper dogMapper;
    private final UserRepository userRepository;

    public List<DogDTO> getUsers() {
        return dogMapper.toDTOList(this.dogRepository.findAll());
    }

    public DogDTO getDogById(UUID id) throws DogNotFoundException {
        Dog dog = this.dogRepository.findById(id).orElseThrow(DogNotFoundException::new);
        return dogMapper.toDTO(dog);
    }

    public List<DogDTO> addDogsToUser(UUID userId, List<DogDTO> dogDTOs) throws UserNotFoundException {
        DBUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + userId));

        List<Dog> dogs = dogMapper.toEntityList(dogDTOs);
        dogs.forEach(dog -> {
            dog.setUser(user);
        });

        List<Dog> savedDogs = dogRepository.saveAll(dogs);
        return dogMapper.toDTOList(savedDogs);
    }
}
