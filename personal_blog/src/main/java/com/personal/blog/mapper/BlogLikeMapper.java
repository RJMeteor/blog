package com.personal.blog.mapper;

import com.personal.blog.pojo.BlogLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.personal.blog.pojo.BlogUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 点赞 Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface BlogLikeMapper extends BaseMapper<BlogLike> {
    /*
     * 每篇文章的点赞总数
     * */
    Long articleContentLike(@Param("id") Long id);

    /*
    * 每篇文章的浏览总数
    * */
    Long articleContentBrowse(@Param("id") Long id);

    /*
    * 用户点赞总数
    * */
    Long userIdAscountLike(@Param("id") Long id);

    /*
    * 用户浏览总数
    * */
    Long userIdAscountBrowse(@Param("id") Long id);

    /*
    * 用户的点赞列表
    * */
//    List<BlogLike> likeList(@Param("id")Long id);

    /*
    * 点赞基本信息
    * */
    BlogLike singleLikeInfo(@Param("id")Long id);

    /*
    * 用户未读点赞列表
    * */
    List<BlogLike> likeListAsActive(@Param("userId")Long userId);

    Integer doLikeAsArticle(BlogLike blogLike);
}
