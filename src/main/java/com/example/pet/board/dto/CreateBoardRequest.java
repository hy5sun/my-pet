package com.example.pet.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBoardRequest {
    
    @NotBlank(message = "제목을 입력하지 않았습니다.")
    private String title;

    @NotBlank(message = "내용을 입력하지 않았습니다.")
    private String content;

    @NotNull(message = "PET HELP 여부를 선택하지 않았습니다.")
    private Boolean isPetHelp;

}
