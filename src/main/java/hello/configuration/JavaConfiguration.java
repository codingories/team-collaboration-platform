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

}


