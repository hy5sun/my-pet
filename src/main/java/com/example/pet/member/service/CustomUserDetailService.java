package com.example.pet.member.service;

import com.example.pet.common.exception.BusinessException;
import com.example.pet.member.domain.Member;
import com.example.pet.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import static com.example.pet.common.exception.ErrorCode.MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .build();
    }
}
