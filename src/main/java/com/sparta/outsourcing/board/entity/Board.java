package com.sparta.outsourcing.board.entity;

import com.sparta.outsourcing.comment.entity.Comment;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.sparta.outsourcing.common.Timestamped;
import com.sparta.outsourcing.user.entity.User;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String generatedname;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "likes")
    private Long like;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @Builder
    public Board(String title, String content, String generatedname, User user) {
        this.title = title;
        this.content = content;
        this.generatedname = generatedname;
        this.user = user;
    }

    public void decreaseLike() {
        this.like--;
    }

    public void increaseLike() {
        this.like++;
    }
}