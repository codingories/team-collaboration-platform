package hello.controller;

import hello.entity.User;
import hello.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

// 改成 @RestController 或者保留 @Controller + 方法上加 @ResponseBody
@RestController
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Inject
    public AuthController( UserService userService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * 判断用户是否登录
     * @return 登录状态 JSON
     */
    @GetMapping("/auth")
    public Object auth() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByUsername(userName);

        if(loggedInUser == null){
            return new Result("ok", "用户没有登录", false );
        } else {
            return new Result("ok", null, true, loggedInUser);
        }
    }

    @PostMapping("/auth/login")
    public Result login(@RequestBody Map<String, String> usernameAndPasswordJson) {
        String username = usernameAndPasswordJson.get("username");
        String password = usernameAndPasswordJson.get("password");
        UserDetails userDetails = null;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return new Result("fail", "用户不存在", false);
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            // 把用户信息保存在一个地方
            // Cookie
            SecurityContextHolder.getContext().setAuthentication(token);
            return new Result("ok", "登录成功", true, userService.getUserByUsername(username));
        } catch (BadCredentialsException e) {
            return new Result("fail", "密码不正确", false);
        }
    }

    // ====================== 新增：注册接口 ======================
    @PostMapping("/auth/register")
    public Result register(@RequestBody Map<String, String> registerJson) {
        String username = registerJson.get("username");
        String password = registerJson.get("password");

        // 用户名校验
        if (username == null || username.trim().isEmpty()) {
            return new Result("fail", "用户名不能为空", false);
        }
        if (username.isEmpty() || username.length() > 15) {
            return new Result("fail", "用户名长度必须在 1-15 个字符之间", false);
        }
        String usernameRegex = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$";

        if (!username.matches(usernameRegex)) {
            return new Result("fail", "用户名只能包含字母、数字、下划线和中文", false);
        }

        // 密码校验
        if (password == null || password.trim().isEmpty()) {
            return new Result("fail", "密码不能为空", false);
        }
        if (password.length() < 6 || password.length() > 16) {
            return new Result("fail", "密码长度必须在 6-16 个字符之间", false);
        }

        try {
            // 保存用户（已加密密码）
            userService.save(username, password);
            return new Result("ok", "注册成功", false);
        } catch (DuplicateKeyException e) {
            // 捕获：用户名重复（数据库唯一约束报错）
            System.out.println("FUCK E"+ e);
            return new Result("fail", "用户名已被注册", false);
        } catch (Exception e) {
            // 捕获其他所有异常
            return new Result("fail", "注册失败，请稍后重试", false);
        }
    }

    @GetMapping("/auth/logout")
    public Result logout() {
        // 获取当前登录用户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 未登录
        if ("anonymousUser".equals(username)) {
            return new Result("fail", "用户尚未登录", false);
        }

        // 已登录 → 执行登出
        SecurityContextHolder.clearContext();
        return new Result("ok", "注销成功", false);
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