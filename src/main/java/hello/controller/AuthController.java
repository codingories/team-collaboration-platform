package hello.controller;

import hello.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// 改成 @RestController 或者保留 @Controller + 方法上加 @ResponseBody
@RestController
public class AuthController {
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * 判断用户是否登录
     * @return 登录状态 JSON
     */
    @GetMapping("/auth")
    public Object auth() {
        return new Result("ok", "用户没有登录", false );
    }

    @PostMapping("/auth/login")
    public Result login(@RequestBody Map<String, String> usernameAndPasswordJson) {
        String username = usernameAndPasswordJson.get("username");
        String password = usernameAndPasswordJson.get("password");
        UserDetails userDetails = null;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return new Result("fail", "用户不存在", false);
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            User loggedInUser = new User( 1, "张三" );
            return new Result("ok", "登录成功", true, loggedInUser);
        } catch (BadCredentialsException e) {
            return new Result("fail", "密码不正确", false);
        }
    }

    public static class Result {
        String status;
        String msg;
        boolean isLogin;
        Object data;

        public Result(String status, String msg, boolean isLogin) {
            this(status, msg, isLogin, null);
        }

        public Result(String status, String msg, boolean isLogin, Object data) {
            this.status = status;
            this.msg = msg;
            this.isLogin = isLogin;
            this.data = data;
        }

        public String getStatus() {
            return status;
        }

        public String getMsg() {
            return msg;
        }

        public boolean isLogin() {
            return isLogin;
        }

        public Object getData() {
            return data;
        }
    }
}