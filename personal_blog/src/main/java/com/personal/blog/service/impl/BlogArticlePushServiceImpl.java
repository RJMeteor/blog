package com.personal.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.personal.blog.cover.EventArticlepush;
import com.personal.blog.cover.coverImpe.EventCover;
import com.personal.blog.pojo.BlogArticlePush;
import com.personal.blog.mapper.BlogArticlePushMapper;
import com.personal.blog.pojo.BlogAttention;
import com.personal.blog.pojo.BlogChattingRecords;
import com.personal.blog.service.IBlogArticlePushService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.blog.utils.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 文章推送 服务实现类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Service
public class BlogArticlePushServiceImpl extends ServiceImpl<BlogArticlePushMapper, BlogArticlePush> implements IBlogArticlePushService {

    @Resource
    private BlogArticlePushMapper blogArticlePushMapper;
    @Resource
    private EventCover eventCover;

    @Override
    public R<List<EventArticlepush>> artclePush(Long userid) {
        List<BlogArticlePush> blogArticlePushes = blogArticlePushMapper.artclePush(userid);
        List<EventArticlepush> eventArticlepushes = eventCover.blogArticlePushListToEventArticlepushList(blogArticlePushes);
        R<List<EventArticlepush>> listR = new R<>();
        listR.success(eventArticlepushes);
        return listR;
    }

    @Override
    public R<String> deleteArticlePush(Long articlePushId) {
        boolean b = this.removeById(articlePushId);
        R<String> stringR = new R<>();
        if (b){
            stringR.success("删除成功");
        }else {
            stringR.error("删除失败");
        }
        return stringR;
    }

    @Override
    public R<String> updateArticlePushAllAsStatus(Long userId) {
        LambdaUpdateWrapper<BlogArticlePush> set = new LambdaUpdateWrapper<BlogArticlePush>()
                .eq(BlogArticlePush::getAcceptUserId, userId)
                .eq(BlogArticlePush::getReadeStatus, 0)
                .set(BlogArticlePush::getReadeStatus, 1);
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
    public R<String> deleteArticlePushAllAsActive(Long userId) {
        LambdaUpdateWrapper<BlogArticlePush> set = new LambdaUpdateWrapper<BlogArticlePush>()
                .eq(BlogArticlePush::getAcceptUserId, userId)
                .eq(BlogArticlePush::getDeleted, 0)
                .set(BlogArticlePush::getDeleted, 1);
        boolean update = this.update(set);
        R<String> stringR = new R<>();
        if (update){
            stringR.success("删除成功");
        }else{
            stringR.error("删除失败");
        }
        return stringR;
    }
}
