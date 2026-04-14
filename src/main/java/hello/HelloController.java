package hello; // 请替换为你的实际包名

import hello.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
public class HelloController {

    private OrderService orderService;

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, SpringBoot! 🎉";
    }

    @Inject
    public HelloController(OrderService orderService) {
        this.orderService = orderService;
    }
}