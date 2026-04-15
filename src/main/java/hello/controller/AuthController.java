package hello.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// 改成 @RestController 或者保留 @Controller + 方法上加 @ResponseBody
@RestController
public class AuthController {

    /**
     * 判断用户是否登录
     * @return 登录状态 JSON
     */
    @GetMapping("/auth")
    public Map<String, Object> auth() {
        Map<String, Object> result = new HashMap<>();

        // 这里先写死未登录，你后面可以替换成真实登录判断
        result.put("status", "ok");
        result.put("isLogin", false);

        return result;
    }

    @PostMapping("/auth/login")
    public void login(@RequestBody Map<String, Object> usernameAndPasswordJson) {
        System.out.println("usernameAndPasswordJson: " + usernameAndPasswordJson);
    }
}