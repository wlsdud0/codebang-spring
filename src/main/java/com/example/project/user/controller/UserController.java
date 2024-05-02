package com.example.project.user.controller;

import com.example.project.user.dto.UserDTO;
import com.example.project.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.stereotype.Controller
@RequiredArgsConstructor
public class UserController {
    // 생성자 주입
    private final UserService userService;

    // 회원가입 페이지 출력 요청
    @GetMapping("user/signup")
    public String signupForm() {
        return "user/signup";
    }
    @PostMapping("user/signup")
    public String signup(@ModelAttribute UserDTO userDTO){
        userService.save(userDTO);
        return "user/login";
    }

    // 로그인 페이지 출력 요청
    @GetMapping("user/login")
    public String loginFrom(HttpSession session) {
        // 세션에 userId가 있는지 확인
        if (session.getAttribute("userId") != null) {
            // 이미 로그인이 되어 있으면 메인 페이지로 리다이렉트
            return "redirect:/";
        }
        // 로그인이 되어있지 않다면 로그인 페이지로 이동
        return "user/login";
    }
    @PostMapping("user/login")
    public String login(@ModelAttribute UserDTO userDTO, HttpSession session) {
        UserDTO loginResult = userService.login(userDTO);

        if (loginResult != null) {
            // login 성공
            session.setAttribute("userIndex", loginResult.getId());
            session.setAttribute("userId", loginResult.getUserId());
            session.setAttribute("userName", loginResult.getUserName());

            return "redirect:/";
        } else {
            // login 실패
            return "user/login";
        }
    }

    // 회원 목록 상세 조회 출력 요청
    @GetMapping("user/profile")
    public String findById(HttpSession session, Model model) {
        String myUserId = (String) session.getAttribute("userId");
        UserDTO userDTO = userService.findById(myUserId);
        model.addAttribute("user", userDTO);
        return "user/profile";
    }

    // 회원정보 수정 출력 요청
    @GetMapping("user/update")
    public String profileForm(HttpSession session, Model model) {
        String myUserId = (String) session.getAttribute("userId");
        UserDTO userDTO = userService.findById(myUserId);
        model.addAttribute("updateUser", userDTO);
        return "user/update";
    }
    @PostMapping("user/update")
    public String update(@ModelAttribute UserDTO userDTO) {
        userService.update(userDTO);
        return "redirect:/user/profile";
    }

    // 회원정보 삭제 출력 요청
    @GetMapping("user/delete/{id}")
    public String deleteById(@PathVariable Long id, HttpSession session) {
        userService.deleteById(id);
        session.invalidate();
        return "redirect:/";
    }

    // 로그아웃
    @GetMapping("user/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // userId-check
    @PostMapping("user/userId-check")
    public @ResponseBody String emailCheck(@RequestParam String userId) {
        return userService.userIdCheck(userId);
    }







    // 관리자 : 회원 목록 페이지 출력 요청
    @GetMapping("/user/")
    public String findAll(Model model) {
        List<UserDTO> userDTOList = userService.findAll();

        // html로 가져갈 어떤 데이터가 있을 때 model 사용
        model.addAttribute("userList", userDTOList);
        return "user/list";
    }

    //    // 회원 목록 상세 조회 출력 요청
//    // @PathVariable: 경로상의 값을 가져올 때 사용
//    @GetMapping("/user/{id}")
//    public String findById(@PathVariable Long id, Model model) {
//        UserDTO userDTO = userService.findById(id);
//        model.addAttribute("user", userDTO);
//        return "user/profile";
//    }
}
