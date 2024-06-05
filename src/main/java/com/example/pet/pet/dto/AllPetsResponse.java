package com.example.pet.pet.dto;

import com.example.pet.pet.domain.Pet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class AllPetsResponse {
    private UUID id;
    private String name;
    private String species;

    public static AllPetsResponse toDto(Pet pet) {
        return new AllPetsResponse(pet.getId(),
                pet.getName(),
                pet.getSpecies()
        );
    }
}
