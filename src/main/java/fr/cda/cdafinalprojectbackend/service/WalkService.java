package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.configuration.security.JwtService;
import fr.cda.cdafinalprojectbackend.dto.dog.DogDTO;
import fr.cda.cdafinalprojectbackend.dto.walk.WalkCreateDTO;
import fr.cda.cdafinalprojectbackend.dto.walk.WalkDTO;
import fr.cda.cdafinalprojectbackend.dto.walk.WalkDetailsDTO;
import fr.cda.cdafinalprojectbackend.dto.walk.WalkLocationDTO;
import fr.cda.cdafinalprojectbackend.entity.DBUser;
import fr.cda.cdafinalprojectbackend.entity.Dog;
import fr.cda.cdafinalprojectbackend.entity.Walk;
import fr.cda.cdafinalprojectbackend.mapper.*;
import fr.cda.cdafinalprojectbackend.repository.WalkRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalkService {
    private final WalkRepository walkRepository;
    private final WalkCreateMapper walkCreateMapper;
    private final WalkMapper walkMapper;
    private final WalkDetailsMapper walkDetailsMapper;
    private final UserMapper userMapper;
    private final DogMapper dogMapper;
    private final JwtService jwtService;

    public WalkDTO createWalk(WalkCreateDTO walkDTO) {
        Walk walk = this.walkCreateMapper.toEntity(walkDTO);
        DBUser user = this.jwtService.getCurrentUser();
        walk.setCreatedBy(user);
        Walk savedWalk = this.walkRepository.save(walk);
        return this.walkMapper.toDTO(savedWalk);
    }

    @Transactional
    public WalkDTO joinWalk(Long walkId) {
        // Récupérer l'utilisateur courant
        DBUser currentUser = this.jwtService.getCurrentUser();
        
        // Récupérer la promenade
        Walk walk = this.walkRepository.findById(walkId)
                .orElseThrow(() -> new EntityNotFoundException("Promenade non trouvée avec l'ID: " + walkId));

        // Vérifier si l'utilisateur est déjà inscrit
        if (walk.getParticipants().contains(currentUser)) {
            return this.walkMapper.toDTO(walk); // L'utilisateur est déjà inscrit, retourner la promenade sans modification
        }
        
        // Vérifier si le nombre maximum de participants est atteint
        if (walk.getParticipantsMax() != null && walk.getParticipants().size() >= walk.getParticipantsMax()) {
            throw new IllegalStateException("Le nombre maximum de participants est atteint pour cette promenade");
        }

        // Vérifier si le créateur de la promenade est l'utilisateur courant
        if (walk.getCreatedBy().getId().equals(currentUser.getId())) {
           throw new IllegalStateException("Vous ne pouvez pas vous inscrire à votre propre promenade");
        }

        walk.getParticipants().add(currentUser);

        Walk updatedWalk = this.walkRepository.save(walk);

        return this.walkMapper.toDTO(updatedWalk);
    }

    public List<WalkLocationDTO> getWalksCloseToUser(double latitude, double longitude, double radius) {
        return this.walkRepository.findWalksCloseToUser(latitude, longitude, radius);
    }

    public WalkDetailsDTO getWalkById(Long id) {
        Walk walk = this.walkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Promenade non trouvée avec l'ID: " + id));
        WalkDetailsDTO walkDetailsDTO = this.walkDetailsMapper.toDTO(walk);
        return walkDetailsDTO;
    }
}
