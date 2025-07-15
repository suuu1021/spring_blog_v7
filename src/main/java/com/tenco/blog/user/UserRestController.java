package com.tenco.blog.user;

import com.tenco.blog._core.common.ApiUtil;
import com.tenco.blog.utils.Define;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController  // @Controller + @ResponseBody
public class UserRestController {

    // @Slf4j 사용 시 자동 선언 됨
    // private static final Logger log = LoggerFactory.getLogger(UserRestController.class)

    private final UserService userService;

    // http://localhost:8080/join
    // 회원 가입 API 설계
    @PostMapping("/join")
    // public ResponseEntity<ApiUtil<UserResponse.JoinDTO>> join() {
    // JSON 형식의 데이터를 추출할 때 @RequestBody 선언
    public ResponseEntity<?> join(@RequestBody UserRequest.JoinDTO reqDTO) {
        log.info("회원 가입 API 호출 - 사용자 명 : {}, 이메일 : {}",
                reqDTO.getUsername(), reqDTO.getEmail());
        reqDTO.validate();

        // 서비스에 위임 처리
        UserResponse.JoinDTO joinUser = userService.join(reqDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiUtil<>(joinUser));
    }

    // http://localhost:8080/login
    // 로그인 API - (POST)
    @PostMapping("/login")
    public ResponseEntity<ApiUtil<UserResponse.LoginDTO>> login(
            @RequestBody UserRequest.LoginDTO reqDTO, HttpSession session) {
        log.info("로그인 API 호출 - 사용자명 : {}", reqDTO.getUsername());
        reqDTO.validate();
        UserResponse.LoginDTO loginUser = userService.login(reqDTO);
        // 세션에 정보 저장
        session.setAttribute(Define.SESSION_USER, loginUser);
        return ResponseEntity.ok(new ApiUtil<>(loginUser));
    }


    // 회원 정보 조회
    @GetMapping("/api/users/{id}")
    public ResponseEntity<ApiUtil<UserResponse.DetailDTO>> getUserInfo(
            @PathVariable(name = "id") Long id, HttpSession session) {
        log.info("회원정보 API 호출 - id : {}", id);
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        UserResponse.DetailDTO userDetail = userService.findUserById(id, sessionUser);
        return ResponseEntity.ok(new ApiUtil<>(userDetail));
    }

    // 회원 정보 수정
    @PutMapping("/api/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(name = "id")Long id,
                                        @RequestBody UserRequest.UpdateDTO updateDTO) {
        // 인증검사는 인터셉터에서 처리됨
        // 유효성 검사
        updateDTO.validate();
        UserResponse.UpdateDTO updateUser = userService.updateById(id, updateDTO);
        return ResponseEntity.ok(new ApiUtil<>(updateUser));
    }


    // 로그아웃 처리
    @GetMapping("/logout")
    public ResponseEntity<ApiUtil<String>> logout(HttpSession session) {
        log.info("로그아웃 API 호출");
        session.invalidate();
        return ResponseEntity.ok(new ApiUtil<>("로그아웃 성공"));
    }

}

