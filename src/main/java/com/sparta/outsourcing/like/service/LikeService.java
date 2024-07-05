package com.sparta.outsourcing.like.service;

import com.sparta.outsourcing.board.entity.Board;
import com.sparta.outsourcing.board.repository.BoardRepository;
import com.sparta.outsourcing.board.service.BoardService;
import com.sparta.outsourcing.comment.entity.Comment;
import com.sparta.outsourcing.comment.repository.CommentRepository;
import com.sparta.outsourcing.comment.service.CommentService;
import com.sparta.outsourcing.like.dto.LikeUserBoardResponseDto;
import com.sparta.outsourcing.like.dto.LikeUserCommentResponseDto;
import com.sparta.outsourcing.like.entity.Like;
import com.sparta.outsourcing.like.entity.LikeType;
import com.sparta.outsourcing.like.repository.LikeRepository;
import com.sparta.outsourcing.user.entity.User;
import io.jsonwebtoken.lang.Objects;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final BoardService boardService;
    private final CommentService commentService;
    private final BoardRepository boardRepository;

    private final int pageSize = 5;
    private final CommentRepository commentRepository;

    public int getLikesCount(Long contentId, LikeType contentType) {
        return likeRepository.countByContentIdAndContentType(contentId, contentType);
    }

    public ResponseEntity<String> toggleBoardLike(Long userId, Long contentId) {
        validateBoardLike(userId, contentId);
        Optional<Like> likeOptional = findLike(userId, contentId, LikeType.BOARD);

        if (likeOptional.isPresent()) {
            likeRepository.delete(likeOptional.get());
            boardService.decreaseBoardLike(contentId);
            return ResponseEntity.ok("Like removed" + HttpStatus.OK.value());
        } else {
            Like like = new Like(userId, contentId, LikeType.BOARD);
            likeRepository.save(like);
            boardService.increaseBoardLike(contentId);
            return ResponseEntity.ok("Like added" + HttpStatus.OK.value());
        }
    }

    public ResponseEntity<String> toggleCommentLike(Long userId, Long contentId) {
        validateCommentLike(userId, contentId);
        Optional<Like> likeOptional = findLike(userId, contentId, LikeType.COMMENT);

        if (likeOptional.isPresent()) {
            likeRepository.delete(likeOptional.get());
            commentService.decreaseCommentLike(contentId);
            return ResponseEntity.ok("Like removed" + HttpStatus.OK.value());
        } else {
            Like like = new Like(userId, contentId, LikeType.COMMENT);
            likeRepository.save(like);
            commentService.increaseCommentLike(contentId);
            return ResponseEntity.ok("Like added" + HttpStatus.OK.value());
        }
    }

    private Optional<Like> findLike(Long userId, Long contentId, LikeType contentType) {
        return likeRepository.findByUserIdAndContentIdAndContentType(userId, contentId, contentType);
    }

    private void validateBoardLike(Long userId, Long contentId) {
        boardService.validateBoardLike(userId, contentId);
    }
    private void validateCommentLike(Long userId, Long contentId) {
        commentService.validateCommentLike(userId, contentId);
    }

    public List<LikeUserBoardResponseDto> getUserBoardLikes(User user, int page) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<Board> boardList = boardRepository.getBoardLikeList(user.getId(), pageRequest.getOffset(), pageRequest.getPageSize());
        List<LikeUserBoardResponseDto> responseDtos = boardList.stream().map(LikeUserBoardResponseDto::new).toList();
        return responseDtos;
    }

    public List<LikeUserCommentResponseDto> getUserCommentLikes(User user, int page) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<Comment> commentList = commentRepository.getCommentLikeList(user.getId(), pageRequest.getOffset(), pageRequest.getPageSize());
        List<LikeUserCommentResponseDto> responseDtos = commentList.stream().map(LikeUserCommentResponseDto::new).toList();
        return responseDtos;
    }
}
