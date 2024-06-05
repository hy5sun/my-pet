package com.example.pet.pet.dto;

import com.example.pet.pet.domain.Pet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class DetailPetResponse {
    private UUID id;
    private String name;
    private String species;
    private Integer age;
    private String gender;

    public static DetailPetResponse toDto(Pet pet) {
        return new DetailPetResponse(pet.getId(),
                pet.getName(),
                pet.getSpecies(),
                pet.getAge(),
                pet.getGender());
    }
}
