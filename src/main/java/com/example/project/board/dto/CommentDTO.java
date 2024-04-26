package com.example.project.board.dto;

import com.example.project.board.entity.CommentEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentDTO {
    private Long id;
    private Long userIndex;
    private String commentWriter;
    private String commentContent;
    private Long boardId;
    private LocalDateTime commentCreatedTime;

    public static CommentDTO toCommentDTO(CommentEntity commentEntity, Long boardId) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentContent(commentEntity.getCommentContent());
        commentDTO.setCommentCreatedTime(commentEntity.getCreatedTime());
//        commentDTO.setBoardId(commentEntity.getBoardEntity().getId()); // Service 메서드에 @Transactional
        commentDTO.setBoardId(boardId);

        commentDTO.setCommentWriter(commentEntity.getUserEntity().getUserName());
        commentDTO.setUserIndex(commentEntity.getUserEntity().getId());

        return commentDTO;
    }
}
