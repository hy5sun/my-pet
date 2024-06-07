package com.example.pet.board.controller;

import com.example.pet.board.dto.*;
import com.example.pet.board.service.BoardService;
import com.example.pet.common.annotation.Login;
import com.example.pet.common.response.CustomResponse;
import com.example.pet.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CustomResponse create(@Validated @RequestPart("board") CreateBoardRequest req, @RequestPart(value = "files", required = false) List<MultipartFile> files, @Login Member member) throws IOException {
        log.info("헤헤" + req.getIsPetHelp());
        DetailBoardResponse board = boardService.createBoard(member, req, files);
        return CustomResponse.response(HttpStatus.CREATED, "게시물을 정상적으로 작성했습니다.", board);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse findAll(@Login Member member, @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size) {
        BoardsWithPaginationResponse boards = boardService.findAll(page, size);

        return CustomResponse.response(HttpStatus.OK, "모든 게시물을 정상적으로 조회했습니다.", boards);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse findById(@PathVariable("id") UUID boardId, @Login Member member) {
        DetailBoardResponse board = boardService.findById(boardId, member);

        return CustomResponse.response(HttpStatus.OK, "게시물을 정상적으로 조회했습니다.", board);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse editBoard(@PathVariable("id") UUID boardId, @Validated @RequestPart("board") UpdateBoardRequest req, @RequestPart(value = "files", required = false) List<MultipartFile> files, @Login Member member) throws IOException {
        DetailBoardResponse board = boardService.editBoard(boardId, req, files, member);

        return CustomResponse.response(HttpStatus.OK, "게시물을 정상적으로 수정했습니다.", board);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse deleteBoard(@PathVariable("id") UUID boardId, @Login Member member) {
        boardService.deleteBoard(boardId, member);
        return CustomResponse.response(HttpStatus.OK, "게시물이 정상적으로 삭제되었습니다.");
    }

    @PostMapping("/{id}/like")
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse increaseLike(@PathVariable("id") UUID boardId, @Login Member member) {
        BoardLikeResponse likeCount = boardService.updateLikeCount(boardId, member);
        return CustomResponse.response(HttpStatus.OK, "좋아요를 눌렀습니다.", likeCount);
    }
}
