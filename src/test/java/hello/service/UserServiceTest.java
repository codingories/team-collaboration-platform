package hello.service;

import mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    BCryptPasswordEncoder mockEncoder;

    @Mock
    UserMapper mockMapper;

    @InjectMocks
    UserService userService;

    @Test
    public void testSave() {
        // ==========================================
        // ✅ Given（给定）：准备条件、模拟行为
        // ==========================================
        Mockito.when(mockEncoder.encode("myPassword")).thenReturn("myEncodedPassword");
        // ==========================================
        // ✅ When（当）：执行要测试的方法
        // ==========================================
        userService.save("myUser", "myPassword");
        // ==========================================
        // ✅ Then（然后）：验证结果是否正确
        // ==========================================
//        断言：必须调用过一次，参数是 ("myUser", "myPassword")
        Mockito.verify(mockMapper).save("myUser", "myEncodedPassword");
    }
}