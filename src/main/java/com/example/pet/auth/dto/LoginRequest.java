package com.example.pet.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message="이메일을 입력하지 않았습니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message="비밀번호를 입력하지 않았습니다.")
    @Size(min=6, message = "비밀번호는 6글자 이상이어야 합니다.")
    private String password;
}
