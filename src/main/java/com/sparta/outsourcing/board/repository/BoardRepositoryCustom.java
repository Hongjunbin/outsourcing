package com.sparta.outsourcing.board.repository;

import com.sparta.outsourcing.board.entity.Board;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepositoryCustom {
    List<Board> getBoardListWithPage(long offset, int pageSize);
    List<Board> getBoardLikeList(long userId, long offset, int pageSize);
    Long boardLikeCount(Long userId);
}
