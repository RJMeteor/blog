package com.personal.blog.mapper;

import com.personal.blog.pojo.BlogFans;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 粉丝 Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface BlogFansMapper extends BaseMapper<BlogFans> {
    /*
    * 用户粉丝数
    * */
    Long userIdAscount(@Param("id")Long id);

    /*
    * 用户的粉丝列表
    * */
    List<BlogFans> fansList(@Param("userId")Long id);
}
