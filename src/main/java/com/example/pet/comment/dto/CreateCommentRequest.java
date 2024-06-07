package com.example.pet.comment.dto;

import com.example.pet.board.domain.Board;
import com.example.pet.comment.domain.Comment;
import com.example.pet.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateCommentRequest {
    @NotBlank(message = "내용을 입력하지 않았습니다.")
    private String content;

    public Comment toEntity(Member member, Board board) {
        return Comment.builder()
                .content(content)
                .member(member)
                .board(board)
                .build();
    }
}
