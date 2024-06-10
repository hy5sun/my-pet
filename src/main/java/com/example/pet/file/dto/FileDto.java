package com.example.pet.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class FileDto {
    private List<String> imageUrl;

    public static FileDto toDto(List<String> urls) {
        return new FileDto(urls);
    }
}



