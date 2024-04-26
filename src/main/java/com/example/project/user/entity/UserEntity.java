package com.example.project.user.entity;

import com.example.project.board.entity.BoardEntity;
import com.example.project.board.entity.CommentEntity;
import com.example.project.user.dto.UserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "user_table")
public class UserEntity {
    @Id // pk 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(unique = true, length = 20) // unique 제약조건 추가
    private String userId;
    @Column(nullable = false, length = 20)
    private String userPassword;
    @Column(length = 20, unique = true)
    private String userName;

    private String userEmail;
    private String userPhone;

    @Enumerated(EnumType.STRING)
    private UserRole role; // 권한

    // 작성글
    // User:Board = 1:N
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardEntity> boardEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentEntity> CommentEntityList = new ArrayList<>();


    public static UserEntity toUserEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userDTO.getUserId());
        userEntity.setUserPassword(userDTO.getUserPassword());
        userEntity.setUserName(userDTO.getUserName());
        userEntity.setUserEmail(userDTO.getUserEmail());
        userEntity.setRole(userDTO.getRole());

        return userEntity;
    }

    public static UserEntity toUpdateUserEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDTO.getId());
        userEntity.setUserId(userDTO.getUserId());
        userEntity.setUserPassword(userDTO.getUserPassword());
        userEntity.setUserName(userDTO.getUserName());
        userEntity.setUserEmail(userDTO.getUserEmail());

        return userEntity;
    }

}
