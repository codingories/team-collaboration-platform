package hello.service;

import hello.dao.BlogDao;
import hello.entity.*;
import hello.request.BlogCreateRequest;
import hello.mapper.BlogMapper;
import hello.request.BlogUpdateRequest;
import hello.result.Result;
import hello.result.VoidResult;
import org.springframework.stereotype.Service;
import hello.result.BlogResult;
import hello.result.SingleBlogResult;

import javax.inject.Inject;
import java.time.Instant;
import java.util.List;

@Service
public class BlogService {
    private final BlogDao blogDao;
    private final UserService userService;
    private final BlogMapper blogMapper;

    @Inject
    public BlogService(BlogDao blogDao, UserService userService, BlogMapper blogMapper) {
        this.blogDao = blogDao;
        this.userService = userService;
        this.blogMapper = blogMapper;
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

    public SingleBlogResult createBlog(BlogCreateRequest request, String username) {
        User currentUser = userService.getUserByUsername(username);
        if (currentUser == null) {
            return SingleBlogResult.fail("用户不存在");
        }

        Blog blog = new Blog();
        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setDescription(request.getDescription());
        blog.setUserId(currentUser.getId());
        blog.setCreatedAt(Instant.now());
        blog.setUpdatedAt(Instant.now());

        blogMapper.insertBlog(blog);
        blog.setUser(currentUser);

        return SingleBlogResult.ok("创建成功", blog);
    }

    public SingleBlogResult getBlog(Long blogId) {
        Blog blog = blogMapper.findById(blogId);
        if (blog == null) {
            return SingleBlogResult.fail("系统异常");
        }

        User user = userService.getUserById(blog.getUserId());
        blog.setUser(user);

        return SingleBlogResult.ok("获取成功", blog);
    }

    public SingleBlogResult updateBlog(Long blogId, BlogUpdateRequest request, String username) {
        Blog blog = blogMapper.findById(blogId);
        if (blog == null) {
            return SingleBlogResult.fail("博客不存在");
        }

        User currentUser = userService.getUserByUsername(username);
        if (!blog.getUserId().equals(currentUser.getId())) {
            return SingleBlogResult.fail("无法修改别人的博客");
        }

        // 只更新传入的字段
        if (request.getTitle() != null) blog.setTitle(request.getTitle());
        if (request.getContent() != null) blog.setContent(request.getContent());
        if (request.getDescription() != null) blog.setDescription(request.getDescription());
        if (request.getAtIndex() != null) blog.setAtIndex(request.getAtIndex());
        blog.setUpdatedAt(Instant.now());

        blogMapper.updateBlog(blog);

        blog.setUser(currentUser);
        return SingleBlogResult.ok("修改成功", blog);
    }

    public Result<Void> deleteBlog(Long blogId, String username) {
        Blog blog = blogMapper.findById(blogId);
        if (blog == null) {
            return VoidResult.fail("博客不存在");
        }
        User user = userService.getUserById(blog.getUserId());
        if (!user.getUsername().equals(username)) {
            return VoidResult.fail("无法删除别人的博客");
        }
        blogMapper.deleteBlogById(blogId);
        return VoidResult.ok("删除成功");
    }
}
