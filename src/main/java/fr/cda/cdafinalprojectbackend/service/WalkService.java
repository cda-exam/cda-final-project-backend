package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.configuration.security.JwtService;
import fr.cda.cdafinalprojectbackend.dto.walk.WalkCreateDTO;
import fr.cda.cdafinalprojectbackend.dto.walk.WalkDTO;
import fr.cda.cdafinalprojectbackend.entity.DBUser;
import fr.cda.cdafinalprojectbackend.entity.Walk;
import fr.cda.cdafinalprojectbackend.mapper.WalkCreateMapper;
import fr.cda.cdafinalprojectbackend.mapper.WalkMapper;
import fr.cda.cdafinalprojectbackend.repository.WalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalkService {
    private final WalkRepository walkRepository;
    private final WalkCreateMapper walkCreateMapper;
    private final WalkMapper walkMapper;
    private final JwtService jwtService;

    public WalkDTO createWalk(WalkCreateDTO walkDTO) {
        Walk walk = this.walkCreateMapper.toEntity(walkDTO);
        DBUser user = this.jwtService.getCurrentUser();
        walk.setCreatedBy(user);
        Walk savedWalk = this.walkRepository.save(walk);
        return this.walkMapper.toDTO(savedWalk);
    }
}
