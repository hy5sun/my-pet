package com.example.pet.board.repository;

import com.example.pet.board.domain.Board;
import com.example.pet.board.domain.LikedBoard;
import com.example.pet.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedBoardRepository extends JpaRepository<LikedBoard, Long> {
    Optional<LikedBoard> findByBoardAndMember(Board board, Member member);
}
