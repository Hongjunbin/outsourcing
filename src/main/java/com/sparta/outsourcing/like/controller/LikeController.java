package com.sparta.outsourcing.like.controller;

import com.sparta.outsourcing.like.dto.LikeCountResponseDto;
import com.sparta.outsourcing.like.dto.LikeUserBoardResponseDto;
import com.sparta.outsourcing.like.dto.LikeUserCommentResponseDto;
import com.sparta.outsourcing.like.entity.Like;
import com.sparta.outsourcing.like.entity.LikeType;
import com.sparta.outsourcing.like.service.LikeService;
import com.sparta.outsourcing.security.UserDetailsImpl;
import com.sparta.outsourcing.user.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/{boardId}/likes/count")
    public ResponseEntity<CommonResponse<LikeCountResponseDto>> getBoardLikes(@PathVariable("boardId") Long boardId) {
        LikeCountResponseDto responseDto = new LikeCountResponseDto(likeService.getLikesCount(boardId, LikeType.BOARD));
        CommonResponse<LikeCountResponseDto> response = new CommonResponse<>("게시글 좋아요 갯수 조회 성공", HttpStatus.OK.value(), responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{boardId}/comments/{commentId}/likes/count")
    public ResponseEntity<CommonResponse<LikeCountResponseDto>> getCommentLikes(
            @PathVariable("boardId") Long boardId,
            @PathVariable("commentId") Long commentId)
    {
        LikeCountResponseDto responseDto = new LikeCountResponseDto(likeService.getLikesCount(commentId, LikeType.COMMENT));
        CommonResponse<LikeCountResponseDto> response = new CommonResponse<>("댓글 좋아요 갯수 조회 성공", HttpStatus.OK.value(), responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{boardId}/likes")
    public ResponseEntity<String> toggleBoardLikes(
            @PathVariable("boardId") Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return likeService.toggleBoardLike(userDetails.getUser().getId(), boardId);
    }

    @PostMapping("{boardId}/comments/{commentId}/likes")
    public ResponseEntity<String> toggleCommentLikes(
            @PathVariable("boardId") Long boardId,
            @PathVariable("commentId") Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return likeService.toggleCommentLike(userDetails.getUser().getId(), commentId);
    }

    @GetMapping("/my-board-likes")
    public ResponseEntity<List<LikeUserBoardResponseDto>> getUserBoardLikes(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0", value = "page") int page)
    {
        List<LikeUserBoardResponseDto> responseDtos = likeService.getUserBoardLikes(userDetails.getUser(), page);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @GetMapping("/my-comment-likes")
    public ResponseEntity<List<LikeUserCommentResponseDto>> getUserCommentLikes(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0", value = "page") int page)
    {
        List<LikeUserCommentResponseDto> responseDtos = likeService.getUserCommentLikes(userDetails.getUser(), page);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }
}
