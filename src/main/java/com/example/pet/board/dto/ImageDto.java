package com.example.pet.board.dto;

import com.example.pet.board.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageDto {
    private String imageUrl;

    public static ImageDto toDto(Image image) {
        return new ImageDto(image.getImageUrl());
    }
}
