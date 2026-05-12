package hello.result;

import hello.entity.Blog;

public class SingleBlogResult extends Result<Blog> {
    private SingleBlogResult(String status, String msg, Blog data) {
        super(status, msg, data);
    }

    public static SingleBlogResult ok(String msg, Blog blog) {
        return new SingleBlogResult("ok", msg, blog);
    }

    public static SingleBlogResult fail(String msg) {
        return new SingleBlogResult("fail", msg, null);
    }
}