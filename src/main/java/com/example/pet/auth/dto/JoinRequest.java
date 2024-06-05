package com.example.pet.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequest {
    @NotBlank(message="이메일을 입력하지 않았습니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message="비밀번호를 입력하지 않았습니다.")
    @Size(min=6, message = "비밀번호는 6글자 이상이어야 합니다.")
    private String password;

    @NotBlank(message="닉네임을 입력하지 않았습니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,8}$", message = "닉네임은 특수문자를 포함하지 않은 2글자 이상 8글자 이하여야 합니다.")
    private String nickname;
}
