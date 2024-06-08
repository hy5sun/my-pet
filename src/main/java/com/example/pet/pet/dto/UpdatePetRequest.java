package com.example.pet.pet.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePetRequest {
    @NotBlank(message = "이름을 입력하지 않았습니다.")
    private String name;

    @NotBlank(message = "종을 입력하지 않았습니다.")
    private String species;

    @NotNull(message = "나이를 입력하지 않았습니다.")
    @Min(value = 1, message = "나이는 1보다 크거나 같아야 합니다.")
    private Integer age;

    @NotBlank(message = "성별을 입력하지 않았습니다.")
    private String gender;
}
