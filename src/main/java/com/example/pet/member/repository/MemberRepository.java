package com.example.pet.member.repository;

import com.example.pet.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByNickname(String nickname);
}
