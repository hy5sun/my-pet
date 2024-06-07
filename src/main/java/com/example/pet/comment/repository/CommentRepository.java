package com.example.pet.comment.repository;

import com.example.pet.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Optional<Comment> findById(UUID id);
}
