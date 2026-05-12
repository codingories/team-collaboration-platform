package hello.controller;

import hello.result.SingleBlogResult;
import hello.request.BlogCreateRequest;
import hello.result.Result;
import hello.service.BlogService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

@Controller
public class BlogController {
    private BlogService blogService;

    @Inject
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/blog")
    @ResponseBody
    public Result getBlogs(@RequestParam("page") Integer page, @RequestParam(value="userId", required = false) Integer userId) {
        if(page == null || page<0) {
            page = 1;
        }
        return blogService.getBlogs(page, 10, userId);
    }

    @PostMapping("/blog")
    @ResponseBody
    public SingleBlogResult createBlog(@RequestBody @Valid BlogCreateRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if ("anonymousUser".equals(username)) {
            return SingleBlogResult.fail("登录后才能操作");
        }

        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            String desc = request.getContent().length() > 100
                    ? request.getContent().substring(0, 100)
                    : request.getContent();
            request.setDescription(desc);
        }

        return blogService.createBlog(request, username);
    }

    @GetMapping("/blog/{blogId}")
    @ResponseBody
    public SingleBlogResult getBlog(@PathVariable Long blogId) {
        return blogService.getBlog(blogId);
    }
}
