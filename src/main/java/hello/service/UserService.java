package hello.service;

import mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

public class UserService {
    private final UserMapper userMapper;

    @Inject
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getUserById(Integer id) {
        return userMapper.findUserById(id);
    }
}
