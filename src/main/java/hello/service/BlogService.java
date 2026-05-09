package hello.service;

import hello.dao.BlogDao;
import hello.entity.Blog;
import hello.entity.BlogResult;
import hello.entity.Result;
import hello.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class BlogService {
    private final BlogDao blogDao;
    private final UserService userService;

    @Inject
    public BlogService(BlogDao blogDao, UserService userService) {
        this.blogDao = blogDao;
        this.userService = userService;
    }

    public BlogResult getBlogs(Integer page, Integer pageSize, Integer userId) {
        try {
            List<Blog> blogs = blogDao.getBlogs(page, pageSize, userId);

            blogs.forEach(blog -> {
               User user = userService.getUserById(blog.getUserId());
               blog.setUser(user);
            });

            int count = blogDao.count(userId);

            int pageCount = (count + pageSize - 1) / pageSize;

            return BlogResult.newResults("ok", "获取成功", blogs, count, page, pageCount);
        } catch (Exception e) {
            return BlogResult.fail("系统异常");
        }
    }
}
