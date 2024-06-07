package com.example.pet.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BoardsWithPaginationResponse {
    private List<AllBoardsDto> boards;
    private PaginationDto pageInfo;

    public static BoardsWithPaginationResponse toDto(List<AllBoardsDto> boards, PaginationDto pagination) {
        return new BoardsWithPaginationResponse(boards, pagination);
    }
}
