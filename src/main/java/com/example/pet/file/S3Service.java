package com.example.pet.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.example.pet.common.exception.BusinessException;
import com.example.pet.file.dto.FileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.example.pet.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public FileDto uploadFiles(List<MultipartFile> files) {
        validateFileSize(files);
        ArrayList<String> urls = new ArrayList<>();

        if (files == null) {
            return FileDto.toDto(urls);
        }

        IntStream.range(0, files.size()).forEach(idx -> {
            MultipartFile file = files.get(idx);
            try {
                urls.add(uploadFile(file));
            } catch (IOException e) {
                throw new BusinessException(FILE_UPLOAD_FAILED);
            }
        });
        return FileDto.toDto(urls);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        try {
            String URL = "https://" + bucket + ".s3." + region + ".amazonaws.com/";
            String folderDir = bucket;
            String savedName = createSaveName(file.getOriginalFilename());
            String fileUrl = URL + savedName;
            log.info("파일 저장 fullPath = {}", fileUrl);

            ObjectMetadata metaData = new ObjectMetadata();
            metaData.setContentType(file.getContentType());
            metaData.setContentLength(file.getSize());

            amazonS3Client.putObject(folderDir, savedName, file.getInputStream(), metaData);
            return fileUrl;
        } catch (IOException e) {
            throw new BusinessException(FILE_UPLOAD_FAILED);
        }
    }

    public void fileDelete(String url) {
        String URL = "https://" + bucket + ".s3." + region + ".amazonaws.com/";
        String fileName = url.substring(URL.length());
        log.info("파일명 " + fileName + "삭제");
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    private void validateFileSize(List<MultipartFile> files) {
        if (files != null && files.size() > 3) {
            throw new BusinessException(TOO_MANY_FILES);
        }
    }

    private String createSaveName(String originalName) {
        String ext = extractExt(originalName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalName) {
        int pos = originalName.lastIndexOf(".");
        return originalName.substring(pos + 1);
    }
}
