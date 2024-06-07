package com.example.pet.board.dto;

import com.example.pet.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AllBoardsDto {
    private String id;
    private String title;
    private String content;
    private String thumbnail;
    private Integer likeCount;
    private Integer commentCount;
    private String writer;
    private Boolean isPetHelp;

    public static AllBoardsDto fromEntity(Board board) {
        String thumbnail = board.getImages().isEmpty() ? null : board.getImages().get(0).getImageUrl();

        return new AllBoardsDto(
                board.getId().toString(),
                board.getTitle(),
                board.getContent(),
                thumbnail,
                board.getLikeCount(),
                board.getComments().size(),
                board.getMember().getNickname(),
                board.getIsPetHelp()
        );
    }
}
