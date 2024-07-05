package com.sparta.outsourcing.user.repository;

import com.sparta.outsourcing.config.TestConfig;
import com.sparta.outsourcing.user.entity.User;
import com.sparta.outsourcing.user.entity.UserStatus;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestConfig.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private void setting() {

        User settingUser = User.builder()
                .userUid("ddww123456")
                .password("Qwer1234!@#")
                .username("준빈")
                .intro("인트로")
                .role(UserStatus.USER)
                .build();

        userRepository.save(settingUser);
    }

    @Test
    @DisplayName("유저 조회 테스트")
    void Test() {
        setting();
        User user = userRepository.findById(1L).orElseThrow(
                () -> new NullPointerException("조회 된 정보 없음")
        );
        assertEquals(1L, user.getId());
    }

}
