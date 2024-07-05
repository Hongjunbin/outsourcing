package com.sparta.outsourcing.comment.repository;

import com.sparta.outsourcing.comment.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepositoryCustom {
    List<Comment> getCommentLikeList(Long userId, long offset, int pageSize);
    Long commentLikeCount(Long userId);
}
