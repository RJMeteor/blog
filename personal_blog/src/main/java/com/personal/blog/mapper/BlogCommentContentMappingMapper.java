package com.personal.blog.mapper;

import com.personal.blog.pojo.BlogChattingRecordsContentMapping;
import com.personal.blog.pojo.BlogComment;
import com.personal.blog.pojo.BlogCommentContentMapping;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 评论内容映射 Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface BlogCommentContentMappingMapper extends BaseMapper<BlogCommentContentMapping> {
    BlogCommentContentMapping contentInfo(@Param("id")Integer id);

   Integer sendComment(BlogCommentContentMapping blogCommentContentMapping);
}
