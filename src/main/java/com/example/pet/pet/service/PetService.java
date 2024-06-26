package com.example.pet.pet.service;

import com.example.pet.common.exception.BusinessException;
import com.example.pet.common.type.GenderType;
import com.example.pet.common.type.SpeciesType;
import com.example.pet.member.domain.Member;
import com.example.pet.pet.domain.Pet;
import com.example.pet.pet.dto.*;
import com.example.pet.pet.repository.PetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.pet.common.exception.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    @Transactional
    public DetailPetResponse createPet(Member member, CreatePetRequest req) {
        String gender = validateGender(req.getGender()).toString();
        String species = validateSpecies(req.getSpecies()).toString();

        Pet pet = Pet.builder()
                .name(req.getName())
                .age(req.getAge())
                .species(species)
                .gender(gender)
                .member(member)
                .build();

        petRepository.save(pet);

        return DetailPetResponse.toDto(pet);
    }

    private GenderType validateGender(String gender) {
        return GenderType.fromGenderType(gender);
    }

    private SpeciesType validateSpecies(String species) {
        return SpeciesType.fromSpeciesType(species);
    }

    @Transactional
    public List<AllPetsResponse> findAllByMember(Member member) {
        List<Pet> pets =  petRepository.findAllByMember(member)
                .orElse(new ArrayList<>());

        return pets.stream().map(AllPetsResponse::toDto)
                .toList();
    }

    @Transactional
    public DetailPetResponse findById(UUID id, Member member) {
        Pet pet = findById(id);
        validateOwner(pet, member);
        return DetailPetResponse.toDto(pet);
    }

    @Transactional
    public DetailPetResponse update(UUID id, Member member, UpdatePetRequest req) {
        String gender = validateGender(req.getGender()).toString();
        String species = validateSpecies(req.getSpecies()).toString();
        Pet pet = findById(id);
        validateOwner(pet, member);
        pet.update(req.getName(), species, req.getAge(), gender);
        return DetailPetResponse.toDto(pet);
    }

    @Transactional
    public void delete(UUID id, Member member) {
        Pet pet = findById(id);
        validateOwner(pet, member);
        petRepository.delete(pet);
    }

    private Pet findById(UUID id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PET_NOT_FOUND));
    }

    private void validateOwner(Pet pet, Member member) {
        if (!member.equals(pet.getMember())) {
            throw new BusinessException(UNAUTHORIZED_MEMBER);
        }
    }
}
