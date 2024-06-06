package com.example.pet.item.domain;

import com.example.pet.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name="item")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String image_url;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String category;

}
