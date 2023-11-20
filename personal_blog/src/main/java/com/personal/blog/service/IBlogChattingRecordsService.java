package com.personal.blog.service;

import com.personal.blog.cover.EventPrivateLetter;
import com.personal.blog.pojo.BlogChattingRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.blog.pojo.BlogWebSocket;
import com.personal.blog.utils.R;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 聊天记录 服务类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface IBlogChattingRecordsService extends IService<BlogChattingRecords> {
    R<List<EventPrivateLetter>> chatListAsActive(Long userId);
    R<List<BlogChattingRecords>>  singleChatList(Long userId,Long sendUserId);
    BlogChattingRecords insertChattingRecords(BlogWebSocket blogWebSocket);
    R<String> deleteChatListAsActive(Long chatid);
    void updateChatListAsStatus(Long acceptorUserId, Long senderUserId);

    R<String> updatePrivateletterAllAsStatus(List<Long> privateId);
    R<String> deletePrivateletterAllAsActive(List<Long> privateId);

}
