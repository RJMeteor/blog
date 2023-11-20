package com.personal.blog.mapper;

import com.personal.blog.pojo.BlogArticlePush;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文章推送 Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface BlogArticlePushMapper extends BaseMapper<BlogArticlePush> {
    /*
    * 用户的文章推送列表
    * */
 List<BlogArticlePush> artclePush(@Param("userId")Long userid);


 /*
 * 添加文章时的文章推送
 * */
 BlogArticlePush  addArtcleAsPush(BlogArticlePush blogArticlePush);
}
