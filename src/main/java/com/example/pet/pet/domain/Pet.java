package com.example.pet.pet.domain;

import com.example.pet.common.domain.BaseTimeEntity;
import com.example.pet.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name="pet")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String species;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @Builder
    public Pet(String name, String species, Integer age, String gender, Member member) {
        this.name = name;
        this.species = species;
        this.age = age;
        this.gender = gender;
        this.member = member;
    }

    public void update(String name, String species, Integer age, String gender) {
        this.name = name;
        this.species = species;
        this.age = age;
        this.gender = gender;
    }
}
