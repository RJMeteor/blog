package com.personal.blog.service;

import com.personal.blog.cover.EventLike;
import com.personal.blog.pojo.BlogLike;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.blog.utils.R;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 点赞 服务类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface IBlogLikeService extends IService<BlogLike> {
    //    R<List<BlogLike>> likeList(Long id);
    R<List<EventLike>> likeListAsActive(Long id);

    R<String> deleteLikeListAsActive(Long chatListId);

    R<String> doLikeAsArticle(BlogLike blogLike);

    void doBrowseAsArticle(BlogLike blogLike);

    R<String> deleteLikeAsArticle(Long userId, Long articleId);

    R<String> deleteLikeAllAsActive(Long userId);

    R<String> updateLikeAllAsStatus(Long userId);
}
