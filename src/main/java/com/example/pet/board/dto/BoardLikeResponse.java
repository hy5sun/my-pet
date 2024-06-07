package com.example.pet.board.dto;

import com.example.pet.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardLikeResponse {
    private Integer likeCount;

    public static BoardLikeResponse toDto(Board board) {
        return new BoardLikeResponse(board.getLikeCount());
    }
}
