package com.example.pet.board.dto;

import com.example.pet.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class DetailBoardResponse {
    private UUID id;
    private String title;
    private String content;
    private List<ImageDto> images;
    private String writer;
    private Integer likeCount;
    private Integer CommentCount;
    private Boolean isPetHelp;
    private LocalDateTime createdAt;

    public static DetailBoardResponse toDto(Board board) {
        Integer CommentCount = board.getComments() == null ? 0 : board.getComments().size();

        return new DetailBoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getImages().stream().map(ImageDto::toDto).toList(),
                board.getMember().getNickname(),
                board.getLikeCount(),
                CommentCount,
                board.getIsPetHelp(),
                board.getCreatedAt()
        );
    }
}
