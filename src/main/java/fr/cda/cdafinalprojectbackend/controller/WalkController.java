package fr.cda.cdafinalprojectbackend.controller;

import fr.cda.cdafinalprojectbackend.dto.walk.WalkCreateDTO;
import fr.cda.cdafinalprojectbackend.dto.walk.WalkDTO;
import fr.cda.cdafinalprojectbackend.service.WalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WalkController {
    private final WalkService walkService;

    @PostMapping("/walks")
    public WalkDTO createWalk(@RequestBody WalkCreateDTO walk) {
        return this.walkService.createWalk(walk);
    }
}
