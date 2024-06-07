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
}
