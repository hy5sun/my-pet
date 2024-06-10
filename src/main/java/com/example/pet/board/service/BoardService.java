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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public DetailBoardResponse createBoard(Member member, CreateBoardRequest req) {
        List<Image> images = makeImage(req.getImageUrl());

        Board board = Board.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .isPetHelp(req.getIsPetHelp())
                .images(images)
                .member(member)
                .build();

        boardRepository.save(board);

        images.forEach(image -> {
            image.setBoard(board);
        });

        imageRepository.saveAll(images);

        return DetailBoardResponse.toDto(board);
    }

    @Transactional
    public BoardsWithPaginationResponse findAll(Integer page, Integer size) {
        Pageable pageable = makePageable(page, size);
        Page<Board> boards = findAllWithPageable(pageable);
        return makeAllBoardsResponse(boards);
    }

    private List<Image> makeImage(List<String> urls) {
        return urls.stream()
                .map(url -> Image.builder().imageUrl(url).build())
                .collect(Collectors.toList());
    }

    private List<Image> makeImage(List<String> urls, Board board) {
        return urls.stream()
                .map(url -> Image.builder().imageUrl(url).board(board).build())
                .collect(Collectors.toList());
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
    public DetailBoardResponse editBoard(UUID id, UpdateBoardRequest req, Member member) {
        Board board = getById(id);
        validateAuthor(board, member);

        deleteFile(board.getId());

        List<Image> images = makeImage(req.getImageUrl(), board);

        imageRepository.saveAll(images);
        board.update(req.getTitle(), req.getContent(), req.getIsPetHelp(), images);
        return DetailBoardResponse.toDto(board);
    }

    @Transactional
    public void deleteBoard(UUID id, Member member) {
        Board board = getById(id);
        validateAuthor(board, member);
        deleteFile(id);
        boardRepository.delete(board);
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

    private List<Image> getFilesFromDB(UUID id) {
        return imageRepository.findAllByBoard_Id(id)
                .orElseThrow(() -> new BusinessException(IMAGE_NOT_FOUND));
    }

    private void deleteFromDB(UUID id) {
        List<Image> images = getFilesFromDB(id);
        imageRepository.deleteAll(images);
    }

    private void deleteFile(UUID id) {
        List<String> urls = getFilesFromDB(id).stream().map(image -> image.getImageUrl()).toList();
        urls.forEach(url -> {
            s3Service.fileDelete(url);
        });
        deleteFromDB(id);
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
