package com.example.pet.pet.service;

import com.example.pet.common.type.GenderType;
import com.example.pet.member.domain.Member;
import com.example.pet.pet.domain.Pet;
import com.example.pet.pet.dto.*;
import com.example.pet.pet.repository.PetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    @Transactional
    public DetailPetResponse createPet(Member member, CreatePetRequest req) {
        String gender = validateGender(req.getGender()).toString();

        Pet pet = Pet.builder()
                .name(req.getName())
                .age(req.getAge())
                .species(req.getSpecies())
                .gender(gender)
                .member(member)
                .build();

        petRepository.save(pet);

        return DetailPetResponse.toDto(pet);
    }

    private GenderType validateGender(String gender) {
        return GenderType.fromGenderType(gender);
    }

    @Transactional
    public List<AllPetsResponse> findAllByMember(Member member) {
        List<Pet> pets =  petRepository.findAllByMember(member)
                .orElse(new ArrayList<>());

        return pets.stream().map(AllPetsResponse::toDto)
                .toList();
    }
}
