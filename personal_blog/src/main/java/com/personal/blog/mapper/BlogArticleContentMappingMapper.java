package com.personal.blog.mapper;

import com.personal.blog.pojo.BlogArticleContentMapping;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 文章内容映射 Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface BlogArticleContentMappingMapper extends BaseMapper<BlogArticleContentMapping> {
    /*
    * 发布文章
    * */
    void saveContent(BlogArticleContentMapping blogArticleContentMapping);

    /*
    * 文章对应的内容
    * */
    BlogArticleContentMapping articleContent(@Param("article_content_mapping_id") Long id);
}
