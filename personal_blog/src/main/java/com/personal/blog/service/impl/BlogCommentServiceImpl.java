package com.personal.blog.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.personal.blog.cover.EventComment;
import com.personal.blog.cover.coverImpe.EventCover;
import com.personal.blog.mapper.BlogCommentContentMappingMapper;
import com.personal.blog.pojo.*;
import com.personal.blog.mapper.BlogCommentMapper;
import com.personal.blog.service.IBlogArticleService;
import com.personal.blog.service.IBlogCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.blog.utils.EventMetadata;
import com.personal.blog.utils.R;
import com.personal.blog.utils.SseEmitterUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.xml.stream.events.Comment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Service
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment> implements IBlogCommentService {
    @Resource
    private BlogCommentMapper blogCommentMapper;
    @Resource
    private EventCover eventCover;
    @Resource
    private BlogCommentContentMappingMapper blogCommentContentMappingMapper;
    @Resource
    private IBlogArticleService iBlogArticleService;


    /*
     * 推送在列表里的评论
     * */
    @Override
    public R<List<EventComment>> commentListAsActive(Long userId) {
//        LambdaQueryWrapper<BlogArticle> eq = new LambdaQueryWrapper<BlogArticle>()
//                .select(BlogArticle::getId)
//                .eq(BlogArticle::getUserId, userId);
//        List<Object> objects = iBlogArticleService.listObjs(eq);
        List<BlogComment> blogComments = blogCommentMapper.commentListAsActive(userId);
        List<EventComment> eventComments = eventCover.blogCommentListToEventCommentList(blogComments);
        R<List<EventComment>> listR = new R<>();
        listR.success(eventComments);
        return listR;
    }

    /*
     * 把推送消息从列表里剔除
     * */
    @Override
    public R<String> deleteCommentListAsActive(Long commentId) {

        LambdaUpdateWrapper<BlogComment> objectLambdaUpdateWrapper = new LambdaUpdateWrapper<BlogComment>();
        objectLambdaUpdateWrapper.eq(BlogComment::getId, commentId)
                .set(BlogComment::getActive, 1);
        boolean update = this.update(objectLambdaUpdateWrapper);
        R<String> stringR = new R<>();
        if (update) {
            stringR.success("删除成功");
        } else {
            stringR.error("删除失败");
        }
        return stringR;
    }

    /*
     *
     * 处理多级评论
     * */
    @Override
    public List<BlogComment> processMultiLevelComments(List<BlogComment> blogComments) {
        if (ObjectUtils.isEmpty(blogComments)) return null;

        List<BlogComment> getPersonComment = blogComments.stream().filter(ele -> {
            return new Long(0L).equals(ele.getCommentKeyId());
        }).collect(Collectors.toList());


        Map<Long, List<BlogComment>> commentKeyIdMap = blogComments
                .stream()
                .collect(Collectors.groupingBy(blogComment -> {
                    return blogComment.getCommentKeyId();
                }, Collectors.toList()));

        processChildMultiLevelComments(commentKeyIdMap, getPersonComment);
        return getPersonComment;

    }

    @Override
    public R sendComment(BlogComment blogComment) {
        BlogCommentContentMapping commentContentMapping = blogComment.getCommentContentMapping();
        blogCommentContentMappingMapper.sendComment(commentContentMapping);
        Long id = commentContentMapping.getId();
        R stringR = new R<>();
        if (id > 0) {
            blogComment.setCommentContentMappingId(id);
            blogCommentMapper.addComment(blogComment);
            if (blogComment.getId() > 0) {
                SseEmitterUtil sseEmitterUtil = SseEmitterUtil.sses.get(blogComment.getPersonId());
                BlogComment singleComment = blogCommentMapper.getSingleComment(blogComment.getId());
                singleComment.setIsChildren(false);
                singleComment.setChildren(new ArrayList<BlogComment>());
                if (!ObjectUtils.isEmpty(sseEmitterUtil) && (blogComment.getUserId() != blogComment.getPersonId())) {
                    EventComment eventComment = eventCover.blogCommentToEventComment(singleComment);
                    try {
                        sseEmitterUtil.send(EventMetadata.Comment, JSONObject.toJSONString(eventComment));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                stringR.success(singleComment);
            } else {
                stringR.error("评论失败");
            }
        } else {
            stringR.error("评论失败");
        }
        return stringR;
    }

    @Override
    public R<String> sendCommentAsPushList(BlogComment blogComment) {
        BlogCommentContentMapping commentContentMapping = blogComment.getCommentContentMapping();
        blogCommentContentMappingMapper.sendComment(commentContentMapping);
        R<String> stringR = new R<String>();
        blogComment.setCommentContentMappingId(commentContentMapping.getId());
        Integer integer = blogCommentMapper.addComment(blogComment);
        if (integer>0){
            stringR.success("评论成功");
        }else {
            stringR.error("评论失败");
        }
        return stringR;
    }

    @Override
    public R<String> updateCommentAllAsStatus(Long userId) {
        LambdaUpdateWrapper<BlogComment> set = new LambdaUpdateWrapper<BlogComment>()
                .eq(BlogComment::getPersonId, userId)
                .eq(BlogComment::getStated, 0)
                .set(BlogComment::getStated, 1);
        boolean update = this.update(set);
        R<String> stringR = new R<>();
        if (update){
            stringR.success("已读成功");
        }else{
            stringR.error("已读失败");
        }
        return stringR;
    }

    @Override
    public R<String> deleteCommentAllAsActive(Long userId) {
        LambdaUpdateWrapper<BlogComment> set = new LambdaUpdateWrapper<BlogComment>()
                .eq(BlogComment::getPersonId, userId)
                .eq(BlogComment::getActive, 0)
                .set(BlogComment::getActive, 1);
        boolean update = this.update(set);
        R<String> stringR = new R<>();
        if (update){
            stringR.success("删除成功");
        }else{
            stringR.error("删除失败");
        }
        return stringR;
    }

    @Override
    public R<List<BlogComment>> getComment(Long articleId) {
        R<List<BlogComment>> listR = new R<List<BlogComment>>();
        List<BlogComment> blogComments = blogCommentMapper.articleAsComment(articleId);
        listR.success(blogComments);
        return listR;
    }

    @Override
    public void updateCommentListAsStatus(Long commentId) {
        LambdaUpdateWrapper<BlogComment> set = new LambdaUpdateWrapper<BlogComment>()
                .eq(BlogComment::getId, commentId)
                .set(BlogComment::getStated, 1);
        this.update(set);
    }


    public void processChildMultiLevelComments(Map<Long, List<BlogComment>> commentKeyIdMap, List<BlogComment> blogComments) {
        if (ObjectUtils.isEmpty(blogComments)) return;
        for (BlogComment blogComment : blogComments) {
            Long id = blogComment.getId();
            List<BlogComment> blogComments1 = commentKeyIdMap.get(id);
            blogComment.setIsChildren(false);
            blogComment.setChildren(blogComments1);
            if (ObjectUtils.isEmpty(blogComment.getChildren())) {
                blogComment.setChildren(new ArrayList<BlogComment>());
            }
            if (!ObjectUtils.isEmpty(blogComments1)) {
                blogComment.setIsChildren(true);
                blogComment.setChildren(blogComments1);
                processChildMultiLevelComments(commentKeyIdMap, blogComments1);
            }
        }
    }
}
