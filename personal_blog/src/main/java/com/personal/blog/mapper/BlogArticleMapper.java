package com.personal.blog.mapper;

import com.personal.blog.pojo.BlogArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.personal.blog.utils.R;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * 文章 Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface BlogArticleMapper extends BaseMapper<BlogArticle> {


    /*
     *用户发布的文章列表
     * */
    List<BlogArticle> articleList(@Param("userId") Long userId,@Param("page") Integer page,
                                  @Param("pageSize") Integer pageSize);

    /*
     * 所有用户的文章
     * */
    List<BlogArticle> articles(@Param("page") Integer page,
                               @Param("pageSize") Integer pageSize, @Param("name") String name);

    /*
     * 用户发布的文章总数
     * */
    Long userIdAscount(@Param("id") Long id);

    /*
     * 单篇文章
     * */
    BlogArticle singleArticle(@Param("id") Long articleId);


    /*
     * 简单信息，没有实体类数据
     * */
    BlogArticle singleArticleAsSimpleness(@Param("id") Long articleId);

    /*
     * 文章内容对应懂得文章
     * */
    BlogArticle articleContentAsArticle(@Param("articleCommentId") Long articleCommentId);


    /*
     * 添加文章的Id
     * */
    Long articleToId(BlogArticle blogArticle);

}
