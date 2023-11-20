package com.personal.blog.service;

import com.personal.blog.cover.EventAttention;
import com.personal.blog.pojo.BlogAttention;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.blog.utils.R;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 关注 服务类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface IBlogAttentionService extends IService<BlogAttention> {
    R<List<BlogAttention>> list(Long userId);

    R<List<EventAttention>> attentionListAsActvie(Long id);

    R<String> attention(BlogAttention blogAttention);

    R<String> deleteAsttentionListAsActive(Long asttentionId);
    R<String> deleteAttentionAllAsActive(Long userId);
    R<String> updateAttentionAllAsStatus(Long userId);
}
