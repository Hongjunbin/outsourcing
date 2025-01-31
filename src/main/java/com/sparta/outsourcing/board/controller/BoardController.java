package com.sparta.outsourcing.board.controller;

import com.sparta.outsourcing.board.dto.BoardCreateRequest;
import com.sparta.outsourcing.board.dto.BoardDetailResponseDto;
import com.sparta.outsourcing.board.dto.BoardListResponseDto;
import com.sparta.outsourcing.board.dto.BoardUpdateRequest;
import com.sparta.outsourcing.board.service.BoardService;
import com.sparta.outsourcing.security.UserDetailsImpl;
import com.sparta.outsourcing.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 게시글 작성
    @PostMapping
    public ResponseEntity<String> create(@Valid  @RequestBody BoardCreateRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        boardService.createBoard(request, user);
        return ResponseEntity.status(201).body("게시글이 작성되었습니다.");
    }

    @GetMapping
    public ResponseEntity<List<BoardListResponseDto>> getBoardList(
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "5", value = "size") int size) {
        List<BoardListResponseDto> boardList = boardService.getBoardListWithPage(page, size);
        return new ResponseEntity<>(boardList, HttpStatus.OK);
    }

    // 게시글 선택 조회 : 누구든 조회 가능, 하나의 게시글만 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDetailResponseDto> getBoardDetail(@PathVariable Long boardId) {
        BoardDetailResponseDto response = boardService.getBoardDetail(boardId);
        return ResponseEntity.ok(response);
    }

    // 게시글 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<String> updateBoard(@PathVariable Long boardId, @Valid @RequestBody BoardUpdateRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        boardService.updateBoard(boardId, request, user);
        return ResponseEntity.ok("게시글 수정이 완료되었습니다.");
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        boardService.deleteBoard(boardId, user);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }
}
