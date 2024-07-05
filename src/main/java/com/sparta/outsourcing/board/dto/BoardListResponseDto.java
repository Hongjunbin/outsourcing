package com.sparta.outsourcing.board.dto;

import com.sparta.outsourcing.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardListResponseDto {
    private Long id;
    private String title;
    private String username;
    private LocalDateTime updatedAt;

    public BoardListResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUser().getUsername();
        this.updatedAt = board.getUpdatedAt();
    }
}
