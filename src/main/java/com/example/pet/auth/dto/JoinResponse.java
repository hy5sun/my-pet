package com.example.pet.auth.dto;

import com.example.pet.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JoinResponse {
    private String email;
    private String nickname;

    public static JoinResponse toDto(Member member) {
        return new JoinResponse(member.getEmail(), member.getNickname());
    }
}
