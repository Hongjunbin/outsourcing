package com.sparta.outsourcing.board.service;

import com.sparta.outsourcing.board.dto.BoardCreateRequest;
import com.sparta.outsourcing.board.dto.BoardDetailResponseDto;
import com.sparta.outsourcing.board.dto.BoardListResponseDto;
import com.sparta.outsourcing.board.dto.BoardUpdateRequest;
import com.sparta.outsourcing.board.entity.Board;
import com.sparta.outsourcing.board.repository.BoardRepository;
import com.sparta.outsourcing.comment.repository.CommentRepository;
import com.sparta.outsourcing.common.AnonymousNameGenerator;
import com.sparta.outsourcing.exception.ForbiddenException;
import com.sparta.outsourcing.exception.NotFoundException;
import com.sparta.outsourcing.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Long createBoard(BoardCreateRequest request, User user) {
        Board board = new Board();
        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        board.setUser(user);
        board.setGeneratedname(AnonymousNameGenerator.nameGenerate());
        board.setLike(0L);
        Board savedBoard = boardRepository.save(board);
        return savedBoard.getId();
    }

    public List<BoardListResponseDto> getBoardListWithPage(int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);

        List<Board> boardPageList = boardRepository.getBoardListWithPage(
                pageRequest.getOffset(), pageRequest.getPageSize());

        return boardPageList.stream().map(BoardListResponseDto::new).toList();
    }

    public BoardDetailResponseDto getBoardDetail(Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            BoardDetailResponseDto.BoardData boardData = new BoardDetailResponseDto.BoardData(
                    board.getTitle(),
                    board.getContent(),
                    board.getGeneratedname(),
                    board.getUpdatedAt()
            );
            return new BoardDetailResponseDto(200, "게시글 조회 성공", boardData);
        } else {
            throw new NotFoundException(boardId + "번 게시글을 찾을 수 없습니다.");
        }
    }

    public void updateBoard(Long boardId, BoardUpdateRequest request, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        if (!board.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("작성자만 수정 가능합니다.");
        }
        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        boardRepository.save(board);
    }

    public void deleteBoard(Long boardId, User user) { // 게시글 삭제 기능 추가
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        if (!board.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("작성자만 삭제 가능합니다.");
        }
        boardRepository.delete(board);
    }

    @Transactional
    public void decreaseBoardLike(Long contentId) {
        Board board = boardRepository.findById(contentId).orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        board.decreaseLike();
    }

    @Transactional
    public void increaseBoardLike(Long contentId) {
        Board board = boardRepository.findById(contentId).orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        board.increaseLike();
    }

    public void validateBoardLike(Long userId, Long contentId) {
        Board board = boardRepository.findById(contentId).orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        if(board.getUser().getId().equals(userId)) {
            throw new ForbiddenException("해당 게시글의 작성자입니다.");
        }
    }


}
