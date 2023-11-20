package com.personal.blog.service;

import com.personal.blog.cover.EventComment;
import com.personal.blog.pojo.BlogComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.blog.utils.R;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.stream.events.Comment;
import java.util.List;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface IBlogCommentService extends IService<BlogComment> {
    R<List<EventComment>> commentListAsActive(Long userId);
    R<String> deleteCommentListAsActive(Long commentId);
    List<BlogComment> processMultiLevelComments(List<BlogComment> blogComments);
    R sendComment(BlogComment blogComment);

    R<List<BlogComment>> getComment(Long articleId);
    void  updateCommentListAsStatus(Long commentId);
    R<String> sendCommentAsPushList(BlogComment blogComment);

    R<String> updateCommentAllAsStatus(Long userId);
    R<String> deleteCommentAllAsActive(Long userId);
}
