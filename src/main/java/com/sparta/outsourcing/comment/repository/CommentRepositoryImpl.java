package com.sparta.outsourcing.comment.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.outsourcing.comment.entity.Comment;
import com.sparta.outsourcing.comment.entity.QComment;
import com.sparta.outsourcing.like.entity.LikeType;
import com.sparta.outsourcing.like.entity.QLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> getCommentLikeList(Long userId, long offset, int pageSize) {

        QLike like = QLike.like;
        QComment comment = QComment.comment1;
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.DESC, comment.createdAt);

        List<Comment> commentList = jpaQueryFactory.selectFrom(comment)
                .where(comment.id.in(JPAExpressions
                        .select(like.contentId)
                        .from(like)
                        .where(like.userId.eq(userId)
                                .and(like.contentType.eq(LikeType.COMMENT)))))
                .offset(offset)
                .limit(pageSize)
                .orderBy(orderSpecifier)
                .fetch();

        return commentList;
    }

    @Override
    public Long commentLikeCount(Long userId) {

        QComment comment = QComment.comment1;
        QLike like = QLike.like;

        Long likeCount = jpaQueryFactory.select(comment.count())
                .from(comment)
                .where(comment.id.in(JPAExpressions
                        .select(like.contentId)
                        .from(like)
                        .where(like.userId.eq(userId)
                                .and(like.contentType.eq(LikeType.COMMENT)))))
                .fetchFirst();
        return likeCount;
    }
}
