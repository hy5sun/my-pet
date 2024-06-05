package com.example.pet.auth.service;

import com.example.pet.auth.dto.JoinRequest;
import com.example.pet.auth.dto.JoinResponse;
import com.example.pet.common.exception.BusinessException;
import com.example.pet.member.domain.Member;
import com.example.pet.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.pet.common.exception.ErrorCode.DUPLICATED_EMAIL;
import static com.example.pet.common.exception.ErrorCode.DUPLICATED_NICKNAME;

@Service
@RequiredArgsConstructor
@EnableWebSecurity
public class AuthService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public JoinResponse join(JoinRequest req) {
        Member member = Member.builder()
                .email(req.getEmail())
                .nickname(req.getNickname())
                .password(bCryptPasswordEncoder.encode(req.getPassword()))
                .build();

        validateDuplicateMember(member);
        memberRepository.save(member);
        return JoinResponse.toDto(member);
    }

    private void validateDuplicateMember(Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new BusinessException(DUPLICATED_EMAIL);
        } else if (memberRepository.existsByNickname(member.getNickname())) {
            throw new BusinessException(DUPLICATED_NICKNAME);
        }
    }

}
