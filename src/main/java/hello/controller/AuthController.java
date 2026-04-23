package hello.controller;

import hello.entity.Result;
import hello.entity.User;
import hello.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$";

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 未登录 或 匿名用户 → 直接返回未登录
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return Result.fail("用户没有登录");
        }

        String userName = authentication.getName();
        User loggedInUser = userService.getUserByUsername(userName);

        return loggedInUser == null
                ? Result.fail("用户没有登路")
                : Result.ok("已登录", true, loggedInUser);
    }

    @PostMapping("/auth/login")
    public Result login(@RequestBody Map<String, String> usernameAndPasswordJson) {
        String username = usernameAndPasswordJson.get("username");
        String password = usernameAndPasswordJson.get("password");
        UserDetails userDetails = null;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return Result.fail("用户不存在");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            // 把用户信息保存在一个地方
            // Cookie
            SecurityContextHolder.getContext().setAuthentication(token);
            return Result.ok("登录成功", true, userService.getUserByUsername(username));
        } catch (BadCredentialsException e) {
            return Result.fail("密码不正确");
        }
    }

    // ====================== 新增：注册接口 ======================
    @PostMapping("/auth/register")
    public Result register(@RequestBody Map<String, String> registerJson) {
        String username = registerJson.get("username");
        String password = registerJson.get("password");

        // 用户名校验
        if (username == null || username.trim().isEmpty()) {
            return Result.fail("用户名不能为空");
        }
        if (username.length() > 15) {
            return Result.fail("用户名长度必须在 1-15 个字符之间");
        }

        if (!username.matches(USERNAME_REGEX)) {
            return Result.fail("用户名只能包含字母、数字、下划线和中文");

        }

        // 密码校验
        if (password == null || password.trim().isEmpty()) {
            return Result.fail("密码不能为空");
        }
        if (password.length() < 6 || password.length() > 16) {
            return Result.fail("密码长度必须在 6-16 个字符之间");
        }

        try {
            // 保存用户（已加密密码）
            userService.save(username, password);
            return Result.ok("注册成功", false);
        } catch (DuplicateKeyException e) {
            // 捕获：用户名重复（数据库唯一约束报错）
            return Result.fail("用户名已被注册");
        } catch (Exception e) {
            // 捕获其他所有异常
            return Result.fail("注册失败，请稍后重试");

        }
    }

    @GetMapping("/auth/logout")
    public Result logout() {
        // 获取当前登录用户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 未登录
        if ("anonymousUser".equals(username)) {
            return Result.fail("用户尚未登录");
        }

        // 已登录 → 执行登出
        SecurityContextHolder.clearContext();
        return Result.ok("注销成功", false);
    }
}