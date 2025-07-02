package fr.cda.cdafinalprojectbackend.controller;

import fr.cda.cdafinalprojectbackend.dto.walk.WalkCreateDTO;
import fr.cda.cdafinalprojectbackend.dto.walk.WalkDTO;
import fr.cda.cdafinalprojectbackend.dto.walk.WalkDetailsDTO;
import fr.cda.cdafinalprojectbackend.dto.walk.WalkLocationDTO;
import fr.cda.cdafinalprojectbackend.service.WalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WalkController {
    private final WalkService walkService;

    @PostMapping("/walks")
    public WalkDTO createWalk(@RequestBody WalkCreateDTO walk) {
        return this.walkService.createWalk(walk);
    }
    
    /**
     * Permet à l'utilisateur courant de s'inscrire à une promenade
     * @param walkId l'identifiant de la promenade
     * @return la promenade mise à jour avec le nouvel inscrit
     */
    @PostMapping("/walks/{walkId}/join")
    public ResponseEntity<WalkDetailsDTO> joinWalk(@PathVariable Long walkId) {
        try {
            WalkDetailsDTO updatedWalk = this.walkService.joinWalk(walkId);
            return ResponseEntity.ok(updatedWalk);
        } catch (IllegalStateException e) {
            // Cas où le nombre maximum de participants est atteint
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            // Autres erreurs (promenade non trouvée, etc.)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/walks/location")
    public List<WalkLocationDTO> getWalksCloseToUser(@RequestParam double latitude, @RequestParam double longitude, @RequestParam double radius) {
        return this.walkService.getWalksCloseToUser(latitude, longitude, radius);
    }

    @GetMapping("/walks/{id}")
    public WalkDetailsDTO getWalkById(@PathVariable Long id) {
        return this.walkService.getWalkById(id);
    }
}