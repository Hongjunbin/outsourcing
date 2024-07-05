package com.sparta.outsourcing.board.repository;

import com.sparta.outsourcing.board.entity.Board;
import com.sparta.outsourcing.config.TestConfig;
import static org.junit.jupiter.api.Assertions.*;

import com.sparta.outsourcing.user.entity.User;
import com.sparta.outsourcing.user.entity.UserStatus;
import com.sparta.outsourcing.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestConfig.class)
public class BoardRepositoryCustomImplTest {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;

    private void settingBoard() {
        Board settingBoard = Board.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .generatedname("익명123141")
                .build();
        boardRepository.save(settingBoard);
    }

    @Test
    void test() {
        settingBoard();
        Board board = boardRepository.findById(1L).orElseThrow(
                () -> new NullPointerException("null")
        );
        assertEquals(1L , board.getId());
    }

}
