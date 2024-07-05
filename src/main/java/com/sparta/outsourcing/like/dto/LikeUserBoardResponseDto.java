package com.sparta.outsourcing.like.dto;

import com.sparta.outsourcing.board.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class LikeUserBoardResponseDto {
    private String title;
    private String content;
    private String generatedname;
    private LocalDateTime created_at;
    private LocalDateTime update_at;

    public LikeUserBoardResponseDto(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.generatedname = board.getGeneratedname();
        this.created_at = board.getCreatedAt();
        this.update_at = board.getUpdatedAt();
    }
}
