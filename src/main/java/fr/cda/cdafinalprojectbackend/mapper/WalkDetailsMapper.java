package fr.cda.cdafinalprojectbackend.mapper;

import fr.cda.cdafinalprojectbackend.dto.dog.DogDTO;
import fr.cda.cdafinalprojectbackend.dto.walk.CreatedUserDTO;
import fr.cda.cdafinalprojectbackend.dto.walk.WalkDetailsDTO;
import fr.cda.cdafinalprojectbackend.entity.Walk;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WalkDetailsMapper {
    private final UserMapper userMapper;
    private final DogMapper dogMapper;

    public WalkDetailsMapper(UserMapper userMapper, DogMapper dogMapper) {
        this.userMapper = userMapper;
        this.dogMapper = dogMapper;
    }

    /**
     * Convertit une entité en dto WalkDTO
     * @param walk l'entité à convertir
     * @return l'entité Walk correspondante
     */
    public WalkDetailsDTO toDTO(Walk walk) {
        if (walk == null) {
            return null;
        }

        WalkDetailsDTO walkDetailsDTO = new WalkDetailsDTO();
        walkDetailsDTO.setId(walk.getId());
        walkDetailsDTO.setDate(walk.getDate());
        walkDetailsDTO.setDuration(walk.getDuration());
        walkDetailsDTO.setParticipantsMax(walk.getParticipantsMax());
        walkDetailsDTO.setDescription(walk.getDescription());
        walkDetailsDTO.setLocation(walk.getLocation());
        walkDetailsDTO.setStartLatitude(walk.getStartLatitude());
        walkDetailsDTO.setStartLongitude(walk.getStartLongitude());

        CreatedUserDTO createdUserDTO = new CreatedUserDTO();
        createdUserDTO.setId(walk.getCreatedBy().getId());
        createdUserDTO.setUsername(walk.getCreatedBy().getNickname());
        walkDetailsDTO.setCreatedBy(createdUserDTO);

        walkDetailsDTO.setParticipantsUser(
                walk.getParticipants().stream()
                        .map(this.userMapper::toDTO)
                        .collect(Collectors.toSet())
        );
        walkDetailsDTO.getParticipantsUser().add(this.userMapper.toDTO(walk.getCreatedBy()));

        Set<DogDTO> dogsDTO = new HashSet<>();
        walk.getParticipants()
                .forEach(user ->
                        user.getDogs()
                                .forEach(dog ->
                                        dogsDTO.add(this.dogMapper.toDTO(dog))
                                )
                );
        walk.getCreatedBy().getDogs().forEach(dog -> dogsDTO.add(this.dogMapper.toDTO(dog)));
        walkDetailsDTO.setParticipantsDog(dogsDTO);

        return walkDetailsDTO;
    }
}