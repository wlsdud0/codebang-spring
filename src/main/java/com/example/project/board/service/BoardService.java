package com.example.project.board.service;

import com.example.project.board.dto.BoardDTO;
import com.example.project.board.entity.BoardEntity;
import com.example.project.board.entity.BoardFileEntity;
import com.example.project.board.repository.BoardFileRepository;
import com.example.project.board.repository.BoardRepository;
import com.example.project.user.entity.UserEntity;
import com.example.project.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final UserRepository userRepository;

    // 글 작성 (DTO -> entity)
    @Transactional
    public void edit(BoardDTO boardDTO) throws IOException {
        // 저장
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserName(boardDTO.getUserName());
        UserEntity userEntity = optionalUserEntity.get();
        BoardEntity boardEntity = BoardEntity.toEditEntity(boardDTO, userEntity);

        if (!boardDTO.getBoardFile().get(0).isEmpty()) { // 파일이 있는 경우
            boardDTO.setFileAttached(1); // 파일이 있는 경우 fileAttached를 1로 설정
            boardEntity = BoardEntity.toEditFileEntity(boardDTO, userEntity);
        } else {
            boardDTO.setFileAttached(0); // 파일이 없는 경우 fileAttached를 0으로 설정
        }

        Long savedId = boardRepository.save(boardEntity).getId();
        BoardEntity board = boardRepository.findById(savedId).orElseThrow(() -> new RuntimeException("Board not found"));

        for (MultipartFile boardFile : boardDTO.getBoardFile()) {
            if (!boardFile.isEmpty()) { // 파일이 비어있지 않은 경우에만 처리
                String originalFilename = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename;

                // 맥 로컬
//                String savePath = "/Users/jinyoung/springboot_img/" + storedFileName;

                // 리눅스 서버
                 String savePath = "/home/gjwlsdud730/springboot_img/" + storedFileName;

                boardFile.transferTo(new File(savePath));

                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }
        }
    }

    // 글 목록
    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();

        // entityList -> DTOList
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }

    // 조회수
    @Transactional // update, delete 쿼리 직접 사용시 처리
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    // 상세 글 조회
    @Transactional
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        } else {
            return null;
        }
    }

    // 수정
    @Transactional
    public BoardDTO update(BoardDTO boardDTO) throws IOException {
        // 유저 정보 조회
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserName(boardDTO.getUserName());
        UserEntity userEntity = optionalUserEntity.get();

        // BoardEntity 조회
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(boardDTO.getId());
        BoardEntity existingBoardEntity = optionalBoardEntity.get();

        // BoardEntity 업데이트
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO, userEntity, existingBoardEntity);

        if (!boardDTO.getBoardFile().isEmpty() && !boardDTO.getBoardFile().get(0).isEmpty()) {
            // 파일이 있는 경우
            boardDTO.setFileAttached(1); // 파일이 있는 경우 fileAttached를 1로 설정
            // 파일 정보 업데이트 로직 (기존 파일 삭제 후 새 파일 저장 등의 처리 필요)
            // 여기서는 단순화를 위해 기존 로직을 유지
            boardEntity = BoardEntity.toUptateFileEntity(boardDTO, userEntity, existingBoardEntity);
        } else {
            // 파일이 없는 경우
            boardDTO.setFileAttached(0); // 파일이 없는 경우 fileAttached를 0으로 설정
            // 기존에 첨부된 파일이 있었다면 삭제하는 로직 추가 필요 (예시에는 포함되어 있지 않음)
        }

        // BoardEntity 저장
        boardRepository.save(boardEntity);

        // 파일 처리
        for (MultipartFile boardFile : boardDTO.getBoardFile()) {
            if (!boardFile.isEmpty()) {
                String originalFilename = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename;

                // 맥 로컬
//                String savePath = "/Users/jinyoung/springboot_img/" + storedFileName;

                // 리눅스 서버
                 String savePath = "/home/gjwlsdud730/springboot_img/" + storedFileName;

                // 파일 저장
                boardFile.transferTo(new File(savePath));

                // BoardFileEntity 생성 및 저장
                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(boardEntity, originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }
        }
        return findById(boardDTO.getId());
    }

    // 글 삭제
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    // 페이징
    @Transactional
    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10; // 한 페이지에 보여줄 글 갯수
        //  정렬 기준은 id 기준으로 내림차순 정렬
        // page 위치에 있는 값은 0부터 시작
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        // 목록: id, 작성자, 제목, 조회, 날짜
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> {
            UserEntity userEntity = board.getUserEntity();

            Long userIndex = (userEntity != null) ? (board.getUserEntity().getId()) : 987654321L;
            String userName = (userEntity != null) ? board.getUserEntity().getUserName() : "알 수 없음";

            return new BoardDTO(
                    board.getId(), userIndex, board.getBoardTitle(), userName, board.getBoardHits(), board.getCreatedTime()
            );
        });
        return boardDTOS;
    }
}