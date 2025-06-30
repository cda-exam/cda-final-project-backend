package fr.cda.cdafinalprojectbackend.controller;

import fr.cda.cdafinalprojectbackend.dto.dog.DogDTO;
import fr.cda.cdafinalprojectbackend.exception.UserNotFoundException;
import fr.cda.cdafinalprojectbackend.service.DogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dogs")
@RequiredArgsConstructor
public class DogController {
    private final DogService dogService;

    @PostMapping("/{userId}")
    public void createDog(@PathVariable UUID userId, @RequestBody DogDTO dogDTO) throws UserNotFoundException {
        dogService.createDog(userId, dogDTO);
    }

    @GetMapping("/{userId}")
    public List<DogDTO> getDogsByUser(@PathVariable UUID userId) { return dogService.getDogsByUser(userId); }
}
