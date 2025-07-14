package com.tenco.blog.temp;

import com.tenco.blog.user.User;
import com.tenco.blog.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // @Controller + @Response
@RequiredArgsConstructor
// 해당 컨트롤러에서 직접 접근 허용 하는 방법
// @CrossOrigin(origins = "*") // 모든 앱에서 요청 허용
public class ApiTest {

    // DI 처리
    private final UserService userService;

    @GetMapping("/api-test/users")
    public User getUsers() {
        return userService.findById(1L);
    }

}
