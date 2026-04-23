package hello.controller;

import hello.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    private MockMvc mvc;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    // 自动把 mock 注入到 controller
    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(authController)
                .addFilter(new CharacterEncodingFilter("UTF-8", true)) // ✅ 核心：强制编码
                .build();
    }

    @Test
    void returnNotLoginByDefault() throws Exception {
        mvc.perform(get("/auth")).andExpect(status().isOk()).andExpect(new ResultMatcher() {
            @Override
            public void match(MvcResult result) throws Exception {
                System.out.println(result.getResponse().getContentAsString());
                Assertions.assertTrue(result.getResponse().getContentAsString().contains("用户没有登录"));
            }
        });
    }

}