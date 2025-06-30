package fr.cda.cdafinalprojectbackend.mapper;

import fr.cda.cdafinalprojectbackend.dto.walk.WalkDTO;
import fr.cda.cdafinalprojectbackend.entity.Walk;
import org.springframework.stereotype.Component;


@Component
public class WalkMapper {
    /**
     * Convertit une entité en dto WalkDTO
     * @param walk l'entité à convertir
     * @return l'entité Walk correspondante
     */
    public WalkDTO toDTO(Walk walk) {
        if (walk == null) {
            return null;
        }

        WalkDTO walkDTO = new WalkDTO();
        walkDTO.setId(walk.getId());
        walkDTO.setDate(walk.getDate());
        walkDTO.setDuration(walk.getDuration());
        walkDTO.setParticipantsMax(walk.getParticipantsMax());
        walkDTO.setDescription(walk.getDescription());
        walkDTO.setLocation(walk.getLocation());
        walkDTO.setStartLatitude(walk.getStartLatitude());
        walkDTO.setStartLongitude(walk.getStartLongitude());
        walkDTO.setCreatedBy(walk.getCreatedBy().getId());
        return walkDTO;
    }
}
