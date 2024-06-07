package com.example.pet.board.service;

import com.example.pet.board.domain.Board;
import com.example.pet.board.domain.Image;
import com.example.pet.board.domain.LikedBoard;
import com.example.pet.board.dto.*;
import com.example.pet.board.repository.BoardRepository;
import com.example.pet.board.repository.ImageRepository;
import com.example.pet.board.repository.LikedBoardRepository;
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
    private final LikedBoardRepository likedBoardRepository;
    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    @Transactional
    public DetailBoardResponse createBoard(Member member, CreateBoardRequest req, List<MultipartFile> files) throws IOException {
        if (files == null) {
            files = new ArrayList<>();
        }

        validateFileSize(files);

        List<Image> images = makeImage(files);

        Board board = Board.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .isPetHelp(req.getIsPetHelp())
                .images(images)
                .member(member)
                .build();

        boardRepository.save(board);

        uploadFile(images, files, board);
        imageRepository.saveAll(images);

        return DetailBoardResponse.toDto(board);
    }

    private void uploadFile(List<Image> images, List<MultipartFile> files, Board board) throws IOException {
        String boardId = board.getId().toString();
        IntStream.range(0, files.size()).forEach(fileIndex -> {
                    try {
                        Image image = images.get(fileIndex);
                        String filePath = s3Service.uploadFile(image, files.get(fileIndex), boardId);
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

    @Transactional
    public DetailBoardResponse editBoard(UUID id, UpdateBoardRequest req, List<MultipartFile> files, Member member) throws IOException {
        if (files == null) {
            files = new ArrayList<>();
        }

        validateFileSize(files);

        Board board = getById(id);
        validateAuthor(board, member);

        deleteExistingFile(id);

        List<Image> images = makeImage(files);
        uploadFile(images, files, board);

        imageRepository.saveAll(images);
        board.update(req.getTitle(), req.getContent(), req.getIsPetHelp(), images);
        return DetailBoardResponse.toDto(board);
    }

    @Transactional
    public void deleteBoard(UUID id, Member member) {
        Board board = getById(id);
        validateAuthor(board, member);
        deleteExistingFile(id);
        boardRepository.delete(board);
    }


    private void validateFileSize(List<MultipartFile> files) {
        if (files.size() > 3) {
            throw new BusinessException(TOO_MANY_FILES);
        }
    }

    @Transactional
    public BoardLikeResponse updateLikeCount(UUID id, Member member) {
        Board board = getById(id);

        if (isAuthor(board, member)) {
            throw new BusinessException(BAD_REQUEST);
        }

        if (!isLiked(board, member)) {
            increaseLikeCount(board, member);
        } else {
            decreaseLikeCount(board, member);
        }

        return BoardLikeResponse.toDto(board);
    }

    public void increaseLikeCount(Board board, Member member) {
        LikedBoard likedBoard = LikedBoard.builder()
                .board(board)
                .member(member)
                .build();
        likedBoardRepository.save(likedBoard);
        board.increaseLikeCount();
    }

    public void decreaseLikeCount(Board board, Member member) {
        LikedBoard likedBoard = likedBoardRepository.findByBoardAndMember(board, member)
                .orElseThrow(() -> new BusinessException(LIKED_BOARD_NOT_FOUND));
        board.decreaseLikeCount();
        likedBoardRepository.delete(likedBoard);
    }

    private List<String> getExistingFileName(UUID id) {
        List<Image> images = getFilesFromDB(id);

        return images.stream().map(image -> image.getSaveName()).toList();
    }

    private List<Image> getFilesFromDB(UUID id) {
        return imageRepository.findAllByBoard_Id(id)
                .orElseThrow(() -> new BusinessException(IMAGE_NOT_FOUND));
    }

    private void deleteFromDB(UUID id) {
        List<Image> images = getFilesFromDB(id);
        imageRepository.deleteAll(images);
    }

    private void deleteFile(UUID id, List<String> fileNames) {
        fileNames.forEach(fileName -> {s3Service.fileDelete(id, fileName);});
        deleteFromDB(id);
    }

    private void deleteExistingFile(UUID id) {
        List<String> fileNames = getExistingFileName(id);
        deleteFile(id, fileNames);
    }

    private Boolean isLiked(Board board, Member member) {
        return likedBoardRepository.findByBoardAndMember(board, member)
                .isPresent();
    }

    public Board getById(UUID id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BOARD_NOT_FOUND));
    }

    public Boolean isAuthor(Board board, Member member) {
        return member.equals(board.getMember());
    }

    public void validateAuthor(Board board, Member member) {
        if (!isAuthor(board, member)) {
            throw new BusinessException(UNAUTHORIZED_MEMBER);
        }
    }
}
