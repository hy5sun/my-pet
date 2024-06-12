package com.example.pet.auth.service;

import com.example.pet.auth.dto.JoinRequest;
import com.example.pet.auth.dto.JoinResponse;
import com.example.pet.auth.dto.LoginRequest;
import com.example.pet.auth.dto.LoginResponse;
import com.example.pet.common.config.jwt.TokenProvider;
import com.example.pet.common.exception.BusinessException;
import com.example.pet.member.domain.Member;
import com.example.pet.member.repository.MemberRepository;
import com.example.pet.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

import static com.example.pet.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@EnableWebSecurity
public class AuthService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;
    private final MemberService memberService;

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

    @Transactional
    public LoginResponse signIn(LoginRequest loginRequest) {
        Member member = memberService.findByEmail(loginRequest.getEmail());

        if(!bCryptPasswordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new BusinessException(WRONG_PASSWORD);
        }

        String accessToken = tokenProvider.generateToken(Duration.ofDays(30), member);

        return LoginResponse.entityToDto(member, accessToken);
    }

    public Member findMemberByToken(String token) {
        UUID memberId = tokenProvider.getMemberId(token);
        return memberService.findById(memberId);
    }
}
