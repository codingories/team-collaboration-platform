package hello.configuration;

import hello.service.OrderService;
import hello.service.UserService;
import mapper.UserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("mapper")
public class JavaConfiguration {
    @Bean
    public UserService userService(UserMapper userMapper) {
        return new UserService(userMapper);
    }
    @Bean
    public OrderService orderService(UserService userService) {  // 从参数传入
        return new OrderService(userService);
    }
}


