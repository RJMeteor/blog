package com.personal.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.personal.blog.cover.EventPrivateLetter;
import com.personal.blog.cover.coverImpe.EventCover;
import com.personal.blog.mapper.BlogChattingRecordsContentMappingMapper;
import com.personal.blog.pojo.*;
import com.personal.blog.mapper.BlogChattingRecordsMapper;
import com.personal.blog.service.IBlogChattingRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.blog.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 聊天记录 服务实现类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Service
public class BlogChattingRecordsServiceImpl extends ServiceImpl<BlogChattingRecordsMapper, BlogChattingRecords> implements IBlogChattingRecordsService {

    @Resource
    private BlogChattingRecordsMapper blogChattingRecordsMapper;
    @Resource
    private BlogChattingRecordsContentMappingMapper blogChattingRecordsContentMappingMapper;
    @Resource
    private EventCover eventCover;

    @Override
    public R<List<EventPrivateLetter>> chatListAsActive(Long userId) {
        List<BlogChattingRecords> blogChattingRecords = new ArrayList<BlogChattingRecords>();
        List<Long> longs = blogChattingRecordsMapper.messageUserIdList();
        for (Long s : longs) {
            BlogChattingRecords blogChattingRecords1 = blogChattingRecordsMapper.chatListAsActive(userId, s,true);
            BlogChattingRecords blogChattingRecords2 = blogChattingRecordsMapper.chatListAsActive(userId, s, false);
            if (!ObjectUtils.isEmpty(blogChattingRecords1)&&blogChattingRecords1.getId().equals(blogChattingRecords2.getId())){
                blogChattingRecords.add(blogChattingRecords1);
            }
        }
        List<EventPrivateLetter> eventPrivateLetters = eventCover.blogChattingRecordsListToEventPrivateLetterList(blogChattingRecords);
        R<List<EventPrivateLetter>> listR = new R<>();
        listR.success(eventPrivateLetters);
        return listR;
    }

    @Override
    public R<List<BlogChattingRecords>> singleChatList(Long userId, Long sendUserId) {
        List<BlogChattingRecords> blogChattingRecords = blogChattingRecordsMapper.singleChatList(userId, sendUserId);
        R<List<BlogChattingRecords>> listR = new R<>();
        listR.success(blogChattingRecords);
        return listR;
    }

    @Override
    public BlogChattingRecords insertChattingRecords(BlogWebSocket blogWebSocket) {
        BlogChattingRecordsContentMapping blogChattingRecordsContentMapping = new BlogChattingRecordsContentMapping();
        blogChattingRecordsContentMapping.setChattingRecordsContent(blogWebSocket.getContent());
        blogChattingRecordsContentMappingMapper.insertChattingRecordsContent(blogChattingRecordsContentMapping);
        if (!ObjectUtils.isEmpty(blogChattingRecordsContentMapping.getId())) {
            BlogChattingRecords blogChattingRecords = new BlogChattingRecords();
            blogChattingRecords.setSenderUserId(blogWebSocket.getSenderUserId());
            blogChattingRecords.setAcceptorUserId(blogWebSocket.getAcceptorUserId());
            blogChattingRecords.setChattingRecordsContentMappingId(blogChattingRecordsContentMapping.getId());
            this.baseMapper.insert(blogChattingRecords);
            BlogChattingRecords blogChattingRecords1 = blogChattingRecordsMapper.inserSingleChat(blogChattingRecords);
            return blogChattingRecords1;
        }
        return null;
    }

    @Override
    public R<String> deleteChatListAsActive(Long chatid) {
        LambdaUpdateWrapper<BlogChattingRecords> objectLambdaUpdateWrapper = new LambdaUpdateWrapper<BlogChattingRecords>();
        objectLambdaUpdateWrapper.eq(BlogChattingRecords::getId,chatid)
                .set(BlogChattingRecords::getActive,1);
        boolean update = this.update(objectLambdaUpdateWrapper);
        R<String> stringR = new R<>();
        if (update){
            stringR.success("删除成功");
        }else {
            stringR.error("删除失败");
        }
        return stringR;
    }

    @Override
    public void updateChatListAsStatus(Long acceptorUserId, Long senderUserId) {
        LambdaUpdateWrapper<BlogChattingRecords> set = new LambdaUpdateWrapper<BlogChattingRecords>()
                .eq(BlogChattingRecords::getSenderUserId, senderUserId)
                .eq(BlogChattingRecords::getAcceptorUserId, acceptorUserId)
                .eq(BlogChattingRecords::getReadeStatus, 0)
                .set(BlogChattingRecords::getReadeStatus, 1);
        this.update(set);
    }

    @Override
    public R<String> updatePrivateletterAllAsStatus(List<Long> privateId) {
        LambdaUpdateWrapper<BlogChattingRecords> set = new LambdaUpdateWrapper<BlogChattingRecords>()
                .in(BlogChattingRecords::getId,privateId)
                .eq(BlogChattingRecords::getReadeStatus, 0)
                .set(BlogChattingRecords::getReadeStatus, 1);
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
    public R<String> deletePrivateletterAllAsActive(List<Long> privateId) {
        LambdaUpdateWrapper<BlogChattingRecords> set = new LambdaUpdateWrapper<BlogChattingRecords>()
                .in(BlogChattingRecords::getId,privateId)
                .eq(BlogChattingRecords::getActive, 0)
                .set(BlogChattingRecords::getActive, 1);
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
