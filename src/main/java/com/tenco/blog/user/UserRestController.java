package com.tenco.blog.user;

import com.tenco.blog._core.common.ApiUtil;
import com.tenco.blog.utils.Define;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController  // @Controller + @ResponseBody
public class UserRestController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody UserRequest.JoinDTO reqDTO, Errors errors) {
        log.info("회원 가입 API 호출 - 사용자 명 : {}, 이메일 : {}",
                reqDTO.getUsername(), reqDTO.getEmail());
        UserResponse.JoinDTO joinUser = userService.join(reqDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiUtil<>(joinUser));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiUtil<UserResponse.LoginDTO>> login(
            @Valid @RequestBody UserRequest.LoginDTO reqDTO, Errors errors, HttpSession session) {
        log.info("로그인 API 호출 - 사용자명 : {}", reqDTO.getUsername());
        UserResponse.LoginDTO loginUser = userService.login(reqDTO);
        // 세션에 정보 저장
        session.setAttribute(Define.SESSION_USER, loginUser);
        return ResponseEntity.ok(new ApiUtil<>(loginUser));
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<ApiUtil<UserResponse.DetailDTO>> getUserInfo(
            @PathVariable(name = "id") Long id, HttpSession session) {
        log.info("회원정보 API 호출 - id : {}", id);
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        UserResponse.DetailDTO userDetail = userService.findUserById(id, sessionUser);
        return ResponseEntity.ok(new ApiUtil<>(userDetail));
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(name = "id")Long id,
                                        @Valid @RequestBody UserRequest.UpdateDTO updateDTO,
                                        Errors errors) {
        UserResponse.UpdateDTO updateUser = userService.updateById(id, updateDTO);
        return ResponseEntity.ok(new ApiUtil<>(updateUser));
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiUtil<String>> logout(HttpSession session) {
        log.info("로그아웃 API 호출");
        session.invalidate();
        return ResponseEntity.ok(new ApiUtil<>("로그아웃 성공"));
    }

}

