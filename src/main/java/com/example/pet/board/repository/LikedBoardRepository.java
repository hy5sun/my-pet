package com.example.pet.board.repository;

import com.example.pet.board.domain.LikedBoard;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LikedBoardRepository extends JpaRepository<LikedBoard, Long> {
}
