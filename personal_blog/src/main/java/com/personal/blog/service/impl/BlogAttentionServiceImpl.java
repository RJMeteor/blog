package com.personal.blog.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.personal.blog.cover.EventAttention;
import com.personal.blog.cover.coverImpe.EventCover;
import com.personal.blog.pojo.BlogAttention;
import com.personal.blog.mapper.BlogAttentionMapper;
import com.personal.blog.pojo.BlogChattingRecords;
import com.personal.blog.pojo.BlogFans;
import com.personal.blog.service.IBlogAttentionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.blog.service.IBlogFansService;
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
 * 关注 服务实现类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Service
public class BlogAttentionServiceImpl extends ServiceImpl<BlogAttentionMapper, BlogAttention> implements IBlogAttentionService {
    @Resource
    private BlogAttentionMapper blogAttentionMapper;
    @Resource
    private EventCover eventCover;
    @Resource
    private IBlogFansService iBlogFansService;

    @Override
    public R<List<BlogAttention>> list(Long userId) {
        List<BlogAttention> blogAttentions = blogAttentionMapper.attentionList(userId);
        R<List<BlogAttention>> listR = new R<>();
        listR.success(blogAttentions);
        return listR;
    }

    @Override
    public R<List<EventAttention>> attentionListAsActvie(Long id) {
        List<BlogAttention> blogAttentions = blogAttentionMapper.attentionListAsActvie(id);
        List<EventAttention> eventAttentions = eventCover.blogAttentionListToEventAttentionList(blogAttentions);
        R<List<EventAttention>> listR = new R<>();
        listR.success(eventAttentions);
        return listR;
    }

    @Override
    public R<String> attention(BlogAttention blogAttention) {
        int insert = this.baseMapper.insert(blogAttention);
        BlogFans blogFans = new BlogFans();
        blogFans.setFansUserId(blogAttention.getUserId());
        blogFans.setUserId(blogAttention.getAttentionUserId());
        iBlogFansService.save(blogFans);
        R<String> stringR = new R<>();
        if (insert > 0) {
            stringR.success("关注成功");
            SseEmitterUtil sseEmitterUtil = SseEmitterUtil.sses.get(blogAttention.getAttentionUserId());
            if (!ObjectUtils.isEmpty(sseEmitterUtil)) {
                BlogAttention blogAttention1 = blogAttentionMapper.attentionMessage(blogAttention);
                String message = JSONObject.toJSONString(eventCover.blogAttentionToEventAttention(blogAttention1));
                try {
                    sseEmitterUtil.send(EventMetadata.Attention, message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            stringR.success("关注失败");
        }
        return stringR;
    }

    @Override
    public R<String> deleteAsttentionListAsActive(Long asttentionId) {
        LambdaUpdateWrapper<BlogAttention> objectLambdaUpdateWrapper = new LambdaUpdateWrapper<BlogAttention>();
        objectLambdaUpdateWrapper.eq(BlogAttention::getId,asttentionId)
                .set(BlogAttention::getActive,1);
        boolean update = this.update(objectLambdaUpdateWrapper);
        R<String> stringR = new R<>();
        if (update){
            stringR.success("删除成功");
        }else {
            stringR.error("删除失败");
        }
        return  stringR;
    }

    @Override
    public R<String> deleteAttentionAllAsActive(Long userId) {
        LambdaUpdateWrapper<BlogAttention> set = new LambdaUpdateWrapper<BlogAttention>()
                .eq(BlogAttention::getUserId, userId)
                .eq(BlogAttention::getActive, 0)
                .set(BlogAttention::getActive, 1);
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
    public R<String> updateAttentionAllAsStatus(Long userId) {
        LambdaUpdateWrapper<BlogAttention> set = new LambdaUpdateWrapper<BlogAttention>()
                .eq(BlogAttention::getUserId, userId)
                .eq(BlogAttention::getStated, 0)
                .set(BlogAttention::getStated, 1);
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
