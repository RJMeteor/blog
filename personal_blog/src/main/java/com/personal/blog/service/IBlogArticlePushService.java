package com.personal.blog.service;

import com.personal.blog.cover.EventArticlepush;
import com.personal.blog.pojo.BlogArticlePush;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.blog.utils.R;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 文章推送 服务类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface IBlogArticlePushService extends IService<BlogArticlePush> {
    R<List<EventArticlepush>> artclePush(Long userid);
    R<String> deleteArticlePush(Long articlePushId);
    R<String> updateArticlePushAllAsStatus(Long userId);
    R<String> deleteArticlePushAllAsActive(Long userId);
}
