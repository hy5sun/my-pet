package com.example.pet.auth.dto;

import com.example.pet.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
    private String nickname;
    private String accessToken;

    public static LoginResponse entityToDto(Member member, String accessToken) {
        return new LoginResponse(member.getNickname(), accessToken);
    }
}
