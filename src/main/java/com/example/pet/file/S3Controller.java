package com.example.pet.file;

import com.example.pet.board.dto.ImageDto;
import com.example.pet.common.annotation.Login;
import com.example.pet.common.response.CustomResponse;
import com.example.pet.file.dto.FileDto;
import com.example.pet.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/images")
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CustomResponse uploadImages(@RequestPart(value = "files", required = false) List<MultipartFile> files, @Login Member member) {
        FileDto image = s3Service.uploadFiles(files);
        return CustomResponse.response(HttpStatus.CREATED, "이미지를 정상적으로 올렸습니다.", image);
    }
}
