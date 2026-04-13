package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// SpringBoot 核心注解，自动扫描、自动配置
@SpringBootApplication
public class Application {

    // 程序入口
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}