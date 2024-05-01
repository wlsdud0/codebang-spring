package com.example.project.board.controller;

import com.example.project.board.dto.CommentDTO;
import com.example.project.board.service.BoardService;
import com.example.project.board.dto.BoardDTO;
import com.example.project.board.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    // 기본페이지와 페이징 요청 메서드
    @GetMapping("board/home")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 10;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = Math.min((startPage + blockLimit - 1), boardList.getTotalPages());

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "board/index";
    }



    // 글 작성
    @GetMapping("board/edit")
    public String editForm(HttpSession session) {
        // 세션에 userId가 있는지 확인
        if (session.getAttribute("userId") == null) {
            // 로그인이 되어 있지 않으면 로그인 페이지로 리다이렉트
            return "redirect:/user/login";
        }
        return "board/edit";
    }

    @PostMapping("board/edit")
    public String edit(@ModelAttribute BoardDTO boardDTO, HttpSession session) throws IOException {
        // 세션에 저장된 userId를 writer에 저장
        String userName = (String) session.getAttribute("userName");
        // DTO에 writer 설정
        boardDTO.setUserName(userName);
        boardService.edit(boardDTO);

        return "redirect:/";
    }

    // 상세 글 조회
    @GetMapping("board/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page=1) Pageable pageable,
                           HttpSession session, CommentDTO commentDTO) {
        // 조회수 증가
        boardService.updateHits(id);

        BoardDTO boardDTO = boardService.findById(id);

        // 세션에 저장된 userId를 writer에 저장
        String userName = (String) session.getAttribute("userName");
        // DTO에 writer 설정
        commentDTO.setCommentWriter(userName);
        model.addAttribute("loggedInUser", commentDTO.getCommentWriter());

        // 댓글 목록 가져오기
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("commentList", commentDTOList);

        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
        return "board/detail";
    }

    // 글 수정
    @GetMapping("board/update/{id}")
    public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDTO);

        Long userIndex = (Long) session.getAttribute("userIndex");
        Long boardDTOUserIndex = boardDTO.getUserIndex();

        if (Objects.equals(userIndex, boardDTOUserIndex)) {
            return "board/update";
        }

        return "redirect:/";
    }

    @PostMapping("board/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model, HttpSession session) throws IOException  {
        // 세션에 저장된 userId를 writer에 저장
        String userName = (String) session.getAttribute("userName");
        // DTO에 writer 설정
        boardDTO.setUserName(userName);

        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);

        return "redirect:/board/" + board.getId();

    }

    // 글 삭제
    @GetMapping("board/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        BoardDTO boardDTO = boardService.findById(id);

        Long userIndex = (Long) session.getAttribute("userIndex");
        Long boardDTOUserIndex = boardDTO.getUserIndex();

        if (Objects.equals(userIndex, boardDTOUserIndex)) {
            boardService.delete(id);
            return "redirect:/";
        }
        return "redirect:/";
    }
}



