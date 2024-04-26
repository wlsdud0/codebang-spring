package com.example.project.board.entity;

import com.example.project.board.dto.CommentDTO;
import com.example.project.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comment_table")
public class CommentEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String commentContent;

    // Board:Comment = 1:N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public static CommentEntity toSaveEntity(CommentDTO commentDTO, BoardEntity boardEntity, UserEntity userEntity) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setUserEntity(userEntity);
        commentEntity.setCommentContent(commentDTO.getCommentContent());
        commentEntity.setBoardEntity(boardEntity);

        return commentEntity;
    }

    // 업데이트
    public void update(CommentDTO commentDTO, BoardEntity boardEntity, UserEntity userEntity) {
        this.setBoardEntity(boardEntity);
        this.setUserEntity(userEntity);
        this.setCommentContent(commentDTO.getCommentContent());
    }
}
