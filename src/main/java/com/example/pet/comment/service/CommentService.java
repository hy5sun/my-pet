package com.example.pet.comment.service;

import com.example.pet.board.domain.Board;
import com.example.pet.board.service.BoardService;
import com.example.pet.comment.domain.Comment;
import com.example.pet.comment.dto.CommentDto;
import com.example.pet.comment.dto.CreateCommentRequest;
import com.example.pet.comment.dto.UpdateCommentRequest;
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

    @Transactional
    public CommentDto editComment(UUID boardId, UUID commentId, UpdateCommentRequest req, Member member) {
        validateBoardExist(boardId);
        Comment comment = findById(commentId);
        validateAuthor(comment, member);
        comment.update(req.getContent());
        return CommentDto.toDto(comment);
    }

    @Transactional
    public void deleteComment(UUID boardId, UUID commentId, Member member) {
        validateBoardExist(boardId);
        Comment comment = findById(commentId);
        validateAuthor(comment, member);
        commentRepository.delete(comment);
    }

    public Comment findById(UUID id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(COMMENT_NOT_FOUND));
    }

    private void validateAuthor(Comment comment, Member member) {
        if (!member.equals(comment.getMember())) {
            throw new BusinessException(UNAUTHORIZED_MEMBER);
        }
    }

    private void validateBoardExist(UUID boardId) {
        boardService.getById(boardId);
    }
}
