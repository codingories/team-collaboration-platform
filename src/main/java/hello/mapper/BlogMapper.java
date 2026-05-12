package hello.mapper;


import hello.entity.Blog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface BlogMapper {

    @Insert("INSERT INTO blog (user_id, title, description, content, created_at, updated_at) " +
            "VALUES (#{userId}, #{title}, #{description}, #{content}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 自动回填主键 id
    void insertBlog(Blog blog);
}