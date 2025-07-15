package com.tenco.blog.user;

import com.tenco.blog._core.common.ApiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    // 로그인 요청
    // 회원 정보 조회
    // 회원 정보 수정
    // 로그아웃 처리

}

