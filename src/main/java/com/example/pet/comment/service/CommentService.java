package com.example.pet.comment.service;

import com.example.pet.board.domain.Board;
import com.example.pet.board.service.BoardService;
import com.example.pet.comment.domain.Comment;
import com.example.pet.comment.dto.CommentDto;
import com.example.pet.comment.dto.CreateCommentRequest;
import com.example.pet.comment.repository.CommentRepository;
import com.example.pet.common.exception.BusinessException;
import com.example.pet.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.pet.common.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final BoardService boardService;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentDto createComment(UUID boardId, CreateCommentRequest req, Member member) {
        Board board = boardService.getById(boardId);
        Comment comment = commentRepository.save(req.toEntity(member, board));
        return CommentDto.toDto(comment);
    }

    @Transactional
    public List<CommentDto> findAllByBoardId(UUID boardId) {
        validateBoardExist(boardId);
        return commentRepository.findByBoardIdOrderByCreatedAtAsc(boardId).stream()
                .map(CommentDto::toDto)
                .collect(Collectors.toList());
    }

    public Comment findById(UUID id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(COMMENT_NOT_FOUND));
    }

    private void validateBoardExist(UUID boardId) {
        boardService.getById(boardId);
    }
}
