package fr.cda.cdafinalprojectbackend.controller;

import fr.cda.cdafinalprojectbackend.dto.dog.DogDTO;
import fr.cda.cdafinalprojectbackend.exception.UserNotFoundException;
import fr.cda.cdafinalprojectbackend.service.DogServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/dogs")
@RequiredArgsConstructor
public class DogController {
    private final DogServiceImpl dogService;

    @PostMapping("/{userId}")
    public void createDog(@PathVariable UUID userId, @RequestBody DogDTO dogDTO) throws UserNotFoundException {
        dogService.createDog(userId, dogDTO);
    }
}
