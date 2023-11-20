package com.personal.blog.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.personal.blog.cover.EventLike;
import com.personal.blog.cover.coverImpe.EventCover;
import com.personal.blog.pojo.BlogComment;
import com.personal.blog.pojo.BlogLike;
import com.personal.blog.mapper.BlogLikeMapper;
import com.personal.blog.service.IBlogLikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.blog.utils.EventMetadata;
import com.personal.blog.utils.R;
import com.personal.blog.utils.SseEmitterUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 点赞 服务实现类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Service
public class BlogLikeServiceImpl extends ServiceImpl<BlogLikeMapper, BlogLike> implements IBlogLikeService {
    @Resource
    private BlogLikeMapper blogLikeMapper;
    @Resource
    private EventCover eventCover;

//    @Override
//    public R<List<BlogLike>> likeList(Long id) {
//        List<BlogLike> blogLikes = blogLikeMapper.likeList(id);
//        R<List<BlogLike>> listR = new R<>();
//        listR.success(blogLikes);
//        return listR;
//    }

    /*
     * 拉取用户的点赞推送列表里的
     * */
    @Override
    public R<List<EventLike>> likeListAsActive(Long userId) {
        List<BlogLike> blogLikes = blogLikeMapper.likeListAsActive(userId);
        List<EventLike> eventLikes = eventCover.blogLikeListToEventLikeList(blogLikes);
        R<List<EventLike>> listR = new R<>();
        listR.success(eventLikes);
        return listR;
    }

    @Override
    public R<String> deleteLikeListAsActive(Long likeId) {
        LambdaUpdateWrapper<BlogLike> objectLambdaUpdateWrapper = new LambdaUpdateWrapper<BlogLike>();
        objectLambdaUpdateWrapper.eq(BlogLike::getId, likeId)
                .set(BlogLike::getActive, 1);
        boolean update = this.update(objectLambdaUpdateWrapper);
        R<String> stringR = new R<>();
        if (update) {
            stringR.success("删除成功");
        } else {
            stringR.error("删除失败");
        }
        return stringR;

    }

    @Override
    public R<String> doLikeAsArticle(BlogLike blogLike) {
        R<String> stringR = new R<>();
        blogLike.setLikeBrowseLimiter(0);
        blogLikeMapper.doLikeAsArticle(blogLike);
        if (blogLike.getId() > 0) {
            if (!blogLike.getUserId().equals(blogLike.getPersonId())) {
                SseEmitterUtil sseEmitterUtil = SseEmitterUtil.sses.get(blogLike.getPersonId());
                if (!ObjectUtils.isEmpty(sseEmitterUtil)) {
                    BlogLike blogLike1 = blogLikeMapper.singleLikeInfo(blogLike.getId());
                    EventLike eventLike = eventCover.blogLikeToEventLike(blogLike1);
                    try {
                        sseEmitterUtil.send(EventMetadata.Like, JSONObject.toJSONString(eventLike));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            stringR.success("点赞成功");
        } else {
            stringR.error("点赞失败");
        }
        return stringR;
    }

    @Override
    public void doBrowseAsArticle(BlogLike blogLike) {
        blogLike.setLikeBrowseLimiter(1);
        LambdaQueryWrapper<BlogLike> eq = new LambdaQueryWrapper<BlogLike>()
                .eq(BlogLike::getUserId, blogLike.getUserId())
                .eq(BlogLike::getPersonId, blogLike.getPersonId())
                .eq(BlogLike::getArticleId, blogLike.getArticleId())
                .eq(BlogLike::getLikeBrowseLimiter,blogLike.getLikeBrowseLimiter());
        if (ObjectUtils.isEmpty(this.list(eq))){
            boolean save = this.save(blogLike);
        }
    }

    @Override
    public R<String> deleteLikeAsArticle(Long userId, Long articleId) {
        R<String> stringR = new R<>();
        LambdaQueryWrapper<BlogLike> eq = new LambdaQueryWrapper<BlogLike>()
                .eq(BlogLike::getUserId, userId)
                .eq(BlogLike::getArticleId, articleId);
        boolean remove = this.remove(eq);
        if (remove) {
            stringR.success("取消点赞成功");
        } else {
            stringR.success("点赞成功");
        }
        return stringR;
    }

    @Override
    public R<String> deleteLikeAllAsActive(Long userId) {
        LambdaUpdateWrapper<BlogLike> set = new LambdaUpdateWrapper<BlogLike>()
                .eq(BlogLike::getPersonId, userId)
                .eq(BlogLike::getLikeBrowseLimiter,0)
                .eq(BlogLike::getActive, 0)
                .set(BlogLike::getActive, 1);
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
    public R<String> updateLikeAllAsStatus(Long userId) {
        LambdaUpdateWrapper<BlogLike> set = new LambdaUpdateWrapper<BlogLike>()
                .eq(BlogLike::getPersonId, userId)
                .eq(BlogLike::getLikeBrowseLimiter,0)
                .eq(BlogLike::getStated, 0)
                .set(BlogLike::getStated, 1);
        boolean update = this.update(set);
        R<String> stringR = new R<>();
        if (update){
            stringR.success("已读成功");
        }else{
            stringR.error("已读失败");
        }
        return stringR;
    }
}
