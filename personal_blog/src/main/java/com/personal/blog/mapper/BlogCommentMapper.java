package com.personal.blog.mapper;

import com.personal.blog.pojo.BlogComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 评论 Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface BlogCommentMapper extends BaseMapper<BlogComment> {

    /*
    * 用户的评论
    * */
   List<BlogComment> chatList(@Param("userId")Long userId);

    /*
     * 未读用户的评论
     * */
//    List<BlogComment> commentListAsActive(@Param("userId")Long userId,List<Object> list);
    List<BlogComment> commentListAsActive(@Param("userId")Long userId);

    /*
    * 文章的评论
    * */
    List<BlogComment> articleAsComment(@Param("articleId")Long articleId);

    /*
    * 单条评论
    * */
    BlogComment getSingleComment(@Param("commentId")Long commentId);

    /*
     * 插入评论
     * */
    Integer addComment(BlogComment blogComment);
}
