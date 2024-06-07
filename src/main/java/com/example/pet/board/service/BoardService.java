package com.example.pet.board.service;

import com.example.pet.board.domain.Board;
import com.example.pet.board.domain.Image;
import com.example.pet.board.dto.*;
import com.example.pet.board.repository.BoardRepository;
import com.example.pet.board.repository.ImageRepository;
import com.example.pet.common.exception.BusinessException;
import com.example.pet.file.S3Service;
import com.example.pet.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.pet.common.exception.ErrorCode.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    @Transactional
    public DetailBoardResponse createBoard(Member member, CreateBoardRequest req, List<MultipartFile> files) throws IOException {
        if (files == null) {
            files = new ArrayList<>();
        }

        List<Image> images = makeImage(files);

        Board board = Board.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .images(images)
                .member(member)
                .build();

        uploadFile(images, files, board);

        boardRepository.save(board);
        imageRepository.saveAll(images);

        return DetailBoardResponse.toDto(board);
    }

    private void uploadFile(List<Image> images, List<MultipartFile> files, Board board) throws IOException {
        IntStream.range(0, files.size()).forEach(fileIndex -> {
                    try {
                        Image image = images.get(fileIndex);
                        String filePath = s3Service.uploadFile(image, files.get(fileIndex), board.getId());
                        image.setImageUrl(filePath);
                        image.setBoard(board);
                    } catch (IOException e) {
                        throw new BusinessException(FILE_UPLOAD_FAILED);
                    }
                });
    }

    private List<Image> makeImage(List<MultipartFile> files) {
        return files.stream()
                .map(image -> Image.builder().originalName(image.getOriginalFilename()).build())
                .collect(Collectors.toList());
    }

    @Transactional
    public BoardsWithPaginationResponse findAll(Integer page, Integer size) {
        Pageable pageable = makePageable(page, size);
        Page<Board> boards = findAllWithPageable(pageable);
        return makeAllBoardsResponse(boards);
    }

    private Pageable makePageable(Integer page, Integer size) {
        return PageRequest.of(page, size, Sort.by("createdAt").descending());
    }

    private Page<Board> findAllWithPageable(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    private BoardsWithPaginationResponse makeAllBoardsResponse(Page<Board> boards) {
        PaginationDto pageInfo = PaginationDto.toDto(boards);

        List<AllBoardsDto> boardsResponse = boards.stream()
                .map(AllBoardsDto::fromEntity)
                .toList();

        return new BoardsWithPaginationResponse(boardsResponse, pageInfo);
    }

    @Transactional
    public DetailBoardResponse findById(UUID id, Member member) {
        Board board = getById(id);
        return DetailBoardResponse.toDto(board);
    }

    public Board getById(UUID id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BOARD_NOT_FOUND));
    }
}
