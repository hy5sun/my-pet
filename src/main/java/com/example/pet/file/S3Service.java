package com.example.pet.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.example.pet.board.domain.Image;
import com.example.pet.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

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

    public String uploadFile(Image image, MultipartFile file, String boardId) throws IOException {
        try {
            String folderDir = bucket + "/" + boardId;
            String savedFilename = image.getSaveName();
            String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + boardId + "/" + savedFilename;
            log.info("파일 저장 fullPath = {}", fileUrl);

            ObjectMetadata metaData = new ObjectMetadata();
            metaData.setContentType(file.getContentType());
            metaData.setContentLength(file.getSize());

            amazonS3Client.putObject(folderDir, savedFilename, file.getInputStream(), metaData);
            return fileUrl;
        } catch (IOException e) {
            throw new BusinessException(FILE_UPLOAD_FAILED);
        }
    }

    public void fileDelete(UUID boardId, String fileName) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket + "/" + boardId, fileName));
    }

}
