package hello.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class BlogCreateRequest {
    @NotBlank(message = "博客标题不能为空")
    @Size(max = 100, message = "博客标题不超过100个字符")
    private String title;

    @NotBlank(message = "博客内容不能为空")
    @Size(max = 10000, message = "博客内容不超过10000个字符")
    private String content;
    private String description; // 可为空

    // Getters & Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}