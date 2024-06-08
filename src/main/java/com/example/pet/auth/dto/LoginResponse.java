package com.example.pet.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
    private String accessToken;

    public static LoginResponse entityToDto(String accessToken) {
        return new LoginResponse(accessToken);
    }
}
