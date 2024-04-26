package com.example.project.board.dto;

import com.example.project.board.entity.BoardEntity;
import com.example.project.board.entity.BoardFileEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long id;
    private Long userIndex;
    private String boardTitle;
    private String boardContents;
    private String userName;
    private int boardHits;
    private LocalDateTime boardCreatedTime;

    private List<MultipartFile> boardFile; // edit.html -> UserController 파일 담는 용도
    private List<String> originalFileName; // 원본 파일 이름
    private List<String> storedFileName; // 서버 저장용 파일 이름
    private int fileAttached; // 파일 첨부 여부 (첨부 1, 미첨부 0)

    // DTO 생성자
    // TODO: 3/1/24  (나중에 user추가)
    public BoardDTO(Long id, Long userIndex, String boardTitle, String userName,int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.userIndex = userIndex;
        this.boardTitle = boardTitle;
        this.userName = userName;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    // entity -> DTO
    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        // getUserEntity()가 null이 아닌 경우에만 처리
        if (boardEntity.getUserEntity() != null) {
            boardDTO.setUserName(boardEntity.getUserEntity().getUserName());
            boardDTO.setUserIndex(boardEntity.getUserEntity().getId());
        } else {
            // userEntity가 null인 경우의 로직 수행
            boardDTO.setUserName(null);
            boardDTO.setUserIndex(null);
        }

        // boardEntity가 null이 아니고, fileAttached가 0이 아닌 경우에만 처리
        if (boardEntity != null && boardEntity.getFileAttached() != 0) {
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();

            boardDTO.setFileAttached(boardEntity.getFileAttached());

            // getBoardFileEntityList가 null이 아닌 경우에만 처리
            if (boardEntity.getBoardFileEntityList() != null) {
                for (BoardFileEntity boardFileEntity : boardEntity.getBoardFileEntityList()) {
                    originalFileNameList.add(boardFileEntity.getOriginalFileName());
                    storedFileNameList.add(boardFileEntity.getStoredFileName());
                }
            }

            boardDTO.setOriginalFileName(originalFileNameList);
            boardDTO.setStoredFileName(storedFileNameList);
        } else {
            // fileAttached가 0인 경우
            boardDTO.setFileAttached(0);
        }

        return boardDTO;
    }
}
