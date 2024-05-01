package com.example.project;

import com.example.project.board.dto.BoardDTO;
import com.example.project.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final BoardService boardService;

    // 기본페이지와 페이징 요청 메서드
    @GetMapping("/")
    public String home() {
        return "redirect:/board/home";
    }
}

