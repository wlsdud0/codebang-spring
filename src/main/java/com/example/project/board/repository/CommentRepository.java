package com.example.project.board.repository;

import com.example.project.board.entity.BoardEntity;
import com.example.project.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    // select * from comment_table where board_id=? ord by id desc;
    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);

//    Optional<CommentEntity> findByCommentContent(String commentContent);
}
