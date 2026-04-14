package hello.service;

import org.springframework.stereotype.Service;

public class UserService {
    public User getUserById(Integer id) {
        return new User(id, "");
    }
}
