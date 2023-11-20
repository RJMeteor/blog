package com.personal.blog.mapper;

import com.personal.blog.pojo.BlogArt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface BlogArtMapper extends BaseMapper<BlogArt> {

    List<BlogArt> blogArtList();

}
