package com.sparta.outsourcing.user.repository;

import com.sparta.outsourcing.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserUid(String userUid);
    Optional<User> findByUserUidAndPassword(String userUid, String password);
}
