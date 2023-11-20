package com.personal.blog.mapper;

import com.personal.blog.pojo.BlogAttention;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 关注 Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface BlogAttentionMapper extends BaseMapper<BlogAttention> {
   /*
   * 用户关注总数
   * */
   Long userIdAscount(@Param("id")Long id);

   /*
   * 用户关注列表ID
   * */
   List<Long> attentionAsId(@Param("id")Long id);


   /*
   * 用户关注列表
   * */
   List<BlogAttention> attentionList(@Param("userId")Long userId);

   /*
   * 未读用户关注列表
   * */
   List<BlogAttention> attentionListAsActvie(@Param("userId")Long userId);

   /*
   * 用户关注消息推送
   * */
   BlogAttention attentionMessage(BlogAttention blogAttention);

}
