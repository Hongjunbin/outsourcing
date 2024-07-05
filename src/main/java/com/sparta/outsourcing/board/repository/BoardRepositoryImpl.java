package com.sparta.outsourcing.board.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.outsourcing.board.entity.Board;
import com.sparta.outsourcing.board.entity.QBoard;
import com.sparta.outsourcing.like.entity.LikeType;
import com.sparta.outsourcing.like.entity.QLike;
import com.sparta.outsourcing.user.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Board> getBoardListWithPage(long offset, int pageSize) {

        QBoard board = QBoard.board;

        List<Board> boardPageList = jpaQueryFactory.select(board)
                .from(board)
                .offset(offset)
                .limit(pageSize)
                .fetch();

        return boardPageList;
    }

    @Override
    public List<Board> getBoardLikeList(long userId, long offset, int pageSize) {
        QBoard board = QBoard.board;
        QLike like = QLike.like;

        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.DESC, board.createdAt);

        List<Board> boardList = jpaQueryFactory.selectFrom(board)
                .where(board.id.in(JPAExpressions
                                .select(like.contentId)
                                .from(like)
                                .where(like.userId.eq(userId)
                                        .and(like.contentType.eq(LikeType.BOARD)))))
                .offset(offset)
                .limit(pageSize)
                .orderBy(orderSpecifier)
                .fetch();

        return boardList;
    }

    public Long boardLikeCount(Long userId) {
        QBoard board = QBoard.board;
        QLike like = QLike.like;

        Long likeCount = jpaQueryFactory.select(board.count())
                .from(board)
                .where(board.id.in(JPAExpressions
                        .select(like.contentId)
                        .from(like)
                        .where(like.userId.eq(userId)
                                .and(like.contentType.eq(LikeType.BOARD)))))
                .fetchFirst();
        return likeCount;
    }
}
