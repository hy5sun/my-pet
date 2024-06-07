package com.example.pet.board.repository;

import com.example.pet.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {
    Optional<Board> findById(UUID id);
    Page<Board> findAll(Pageable pageable);
    Page<Board> findByTitleContainingAndContentContaining(String titleKeyword, String contentKeyword, Pageable pageable);
}
