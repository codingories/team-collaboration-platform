package hello.request;

public class BlogUpdateRequest {
    private String title;
    private String content;
    private String description;
    private Boolean atIndex;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getAtIndex() { return atIndex; }
    public void setAtIndex(Boolean atIndex) { this.atIndex = atIndex; }
}