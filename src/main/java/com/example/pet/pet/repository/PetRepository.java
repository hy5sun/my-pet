package com.example.pet.pet.repository;

import com.example.pet.pet.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PetRepository extends JpaRepository<Pet, UUID> {
}
