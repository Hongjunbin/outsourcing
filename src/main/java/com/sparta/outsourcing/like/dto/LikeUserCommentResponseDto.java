package com.sparta.outsourcing.like.dto;

import com.sparta.outsourcing.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LikeUserCommentResponseDto {

    private String comment;
    private String generatedname;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public LikeUserCommentResponseDto(Comment comment) {
        this.comment = comment.getComment();
        this.generatedname = comment.getGeneratedname();
        this.created_at = comment.getCreatedAt();
        this.updated_at = comment.getUpdatedAt();
    }

}
