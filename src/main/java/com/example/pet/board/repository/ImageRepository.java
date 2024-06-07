package com.example.pet.board.repository;

import com.example.pet.board.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
    Optional<List<Image>> findAllByBoard_Id(UUID boardId);
}
