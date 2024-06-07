package com.example.pet.board.controller;

import com.example.pet.board.dto.CreateBoardRequest;
import com.example.pet.board.dto.DetailBoardResponse;
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
}
