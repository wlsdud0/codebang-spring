package com.example.project.board.entity;

import com.example.project.board.dto.BoardDTO;
import com.example.project.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "board_table")
public class BoardEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String boardTitle;

    @Column
    private String boardContents;

    @Column
    private int boardHits;

    @Column
    private int fileAttached; // 파일 유무 체크

    // Board:User = N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_name", referencedColumnName = "id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFileEntity> boardFileEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    // DTO -> entity
    public static BoardEntity toEditEntity(BoardDTO boardDTO, UserEntity userEntity) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(0); // 파일 없음.
        boardEntity.setUserEntity(userEntity);

        return boardEntity;
    }

    // file edit
    public static BoardEntity toEditFileEntity(BoardDTO boardDTO, UserEntity userEntity) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(1); // 파일 있음.
        boardEntity.setUserEntity(userEntity);

        return boardEntity;
    }

    // update
    public static BoardEntity toUpdateEntity(BoardDTO boardDTO, UserEntity userEntity, BoardEntity existingBoardEntity) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(existingBoardEntity.getId());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(boardDTO.getBoardHits());
        boardEntity.setFileAttached(0); // 파일 없음.
        boardEntity.setUserEntity(userEntity);

        return boardEntity;
    }

    // file update
    public static BoardEntity toUptateFileEntity(BoardDTO boardDTO, UserEntity userEntity, BoardEntity existingBoardEntity) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(existingBoardEntity.getId());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(1); // 파일 있음.
        boardEntity.setUserEntity(userEntity);

        return boardEntity;
    }


}
