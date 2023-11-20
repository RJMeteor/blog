package com.personal.blog.mapper;

import com.personal.blog.pojo.BlogChattingRecordsContentMapping;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 聊天记录关系映射 Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface BlogChattingRecordsContentMappingMapper extends BaseMapper<BlogChattingRecordsContentMapping> {
    BlogChattingRecordsContentMapping contentInfo(@Param("id")Integer id);
    Integer insertChattingRecordsContent(BlogChattingRecordsContentMapping blogChattingRecordsContentMapping);
}
