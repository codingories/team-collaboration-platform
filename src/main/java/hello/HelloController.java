package hello; // 请替换为你的实际包名

import hello.service.OrderService;
import hello.service.User;
import hello.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
public class HelloController {
    private UserService userService;

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, SpringBoot! 🎉";
    }

    @Inject
    public HelloController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public User index() {
        return this.userService.getUserById(1);
    }
}