package fr.cda.cdafinalprojectbackend.mapper;

import fr.cda.cdafinalprojectbackend.dto.walk.WalkCreateDTO;
import fr.cda.cdafinalprojectbackend.entity.Walk;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class WalkCreateMapper {
    /**
     * Convertit un WalkCreateDTO en entité Walk
     * @param walkCreateDTO le DTO à convertir
     * @return l'entité Walk correspondante
     */
    public Walk toEntity(WalkCreateDTO walkCreateDTO) {
        if (walkCreateDTO == null) {
            return null;
        }
        
        Walk walk = new Walk();
        walk.setDate(walkCreateDTO.getDate());
        walk.setDuration(walkCreateDTO.getDuration());
        walk.setParticipantsMax(walkCreateDTO.getParticipantsMax());
        walk.setDescription(walkCreateDTO.getDescription());
        walk.setLocation(walkCreateDTO.getLocation());
        walk.setStartLatitude(walkCreateDTO.getStartLatitude());
        walk.setStartLongitude(walkCreateDTO.getStartLongitude());
        walk.setParticipants(new HashSet<>());
        
        return walk;
    }
}
