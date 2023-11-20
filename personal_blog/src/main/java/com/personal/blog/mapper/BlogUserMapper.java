package com.personal.blog.mapper;

import com.personal.blog.cover.BlogUserCover;
import com.personal.blog.pojo.BlogArticleContentMapping;
import com.personal.blog.pojo.BlogUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface BlogUserMapper extends BaseMapper<BlogUser> {

    /*
    * 用户的基本信息
    * */
    BlogUser articleContent(@Param("user_id") Long id);

    /*
    * 用户全部信息
    * */
    BlogUserCover informationView(@Param("user_id") Long id);

    /*
    * 插入用户
    * */
    Integer insertUserInfo(BlogUser blogUser);
}
