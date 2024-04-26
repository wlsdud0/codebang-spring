package com.example.project.user.repository;

import com.example.project.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 아이디로 회원 정보 조회
    Optional<UserEntity> findByUserId(String UserId);
    Optional<UserEntity> findByUserName(String UserId);

}
