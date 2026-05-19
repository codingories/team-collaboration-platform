package hello.mapper;


import hello.entity.Blog;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BlogMapper {
    @Insert("INSERT INTO blog (user_id, title, description, content, created_at, updated_at) " +
            "VALUES (#{userId}, #{title}, #{description}, #{content}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 自动回填主键 id
    void insertBlog(Blog blog);

    @Select("SELECT * FROM blog WHERE id = #{id}")
    Blog findById(Long id);

    @Update("UPDATE blog SET title = #{title}, content = #{content}, " +
            "description = #{description}, at_index = #{atIndex}, updated_at = #{updatedAt} " +
            "WHERE id = #{id}")
    void updateBlog(Blog blog);

    @Delete("DELETE FROM blog WHERE id = #{id}")
    void deleteBlogById(Long id);
}