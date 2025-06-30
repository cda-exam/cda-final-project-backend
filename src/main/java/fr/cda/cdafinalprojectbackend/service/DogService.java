package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.dto.dog.DogDTO;

import java.util.List;
import java.util.UUID;

public interface DogService {
    List<DogDTO> getDogsByUser(UUID userId);
}