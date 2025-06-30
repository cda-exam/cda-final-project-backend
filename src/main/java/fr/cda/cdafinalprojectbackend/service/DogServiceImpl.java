package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.dto.dog.DogDTO;
import fr.cda.cdafinalprojectbackend.entity.DBUser;
import fr.cda.cdafinalprojectbackend.entity.Dog;
import fr.cda.cdafinalprojectbackend.exception.DogNotFoundException;
import fr.cda.cdafinalprojectbackend.exception.UserNotFoundException;
import fr.cda.cdafinalprojectbackend.mapper.DogMapper;
import fr.cda.cdafinalprojectbackend.repository.DogRepository;
import fr.cda.cdafinalprojectbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public DogDTO createDog(UUID userId, DogDTO dogDTO) throws UserNotFoundException {
        DBUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + userId));

        Dog dog = dogMapper.toEntity(dogDTO);
        dog.setUser(user);

        Dog savedDog = dogRepository.save(dog);
        return dogMapper.toDTO(savedDog);
    }

    @Override
    public List<DogDTO> getDogsByUser(UUID userId) {
        return this.dogMapper.toDTOList(this.dogRepository.findAllByUserId(userId));
    }
}
