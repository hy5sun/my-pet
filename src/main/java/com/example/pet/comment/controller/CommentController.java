package com.example.pet.comment.controller;

import com.example.pet.comment.dto.CommentDto;
import com.example.pet.comment.dto.CreateCommentRequest;
import com.example.pet.comment.dto.UpdateCommentRequest;
import com.example.pet.comment.service.CommentService;
import com.example.pet.common.annotation.Login;
import com.example.pet.common.response.CustomResponse;
import com.example.pet.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CustomResponse createComment(@PathVariable("boardId") UUID boardId, @Validated @RequestBody CreateCommentRequest req, @Login Member member) {
        CommentDto comment = commentService.createComment(boardId, req, member);
        return CustomResponse.response(HttpStatus.CREATED, "댓글을 정상적으로 작성했습니다.", comment);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse findAllByBoardId(@PathVariable("boardId") UUID boardId, @Login Member member) {
        List<CommentDto> comments = commentService.findAllByBoardId(boardId);
        return CustomResponse.response(HttpStatus.OK, "댓글을 정상적으로 조회했습니다.", comments);
    }

    @PutMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse editComment(@PathVariable("boardId") UUID boardId, @PathVariable("commentId") UUID commentId, @Validated @RequestBody UpdateCommentRequest req, @Login Member member) {
        CommentDto comment = commentService.editComment(boardId, commentId, req, member);
        return CustomResponse.response(HttpStatus.OK, "댓글을 정상적으로 수정했습니다.", comment);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse deleteComment(@PathVariable("boardId") UUID boardId, @PathVariable("commentId") UUID commentId, @Login Member member) {
        commentService.deleteComment(boardId, commentId, member);
        return CustomResponse.response(HttpStatus.OK, "댓글을 정상적으로 삭제했습니다.");
    }
}
