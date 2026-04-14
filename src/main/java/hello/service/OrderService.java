package hello.service;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

public class OrderService {
    private final UserService userService;

    @Inject
    public OrderService(UserService userService) {
        this.userService = userService;
    }

    public void placeOrder(Integer userId, String item) {
        userService.getUserById(userId);
    }
}

