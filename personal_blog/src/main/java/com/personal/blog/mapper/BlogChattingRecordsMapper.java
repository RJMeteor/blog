package com.personal.blog.mapper;

import com.personal.blog.pojo.BlogChattingRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 聊天记录 Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface BlogChattingRecordsMapper extends BaseMapper<BlogChattingRecords> {
     /*
     * 用户的聊天记录推送
     * */
    BlogChattingRecords  chatListAsActive(@Param("userId")Long userId,@Param("sendUserId")Long sendUserId,@Param("flag")Boolean flag);

     /*
     * 用户聊天记录的id列表
     * */
     List<Long>  messageUserIdList();
     /*
     * 用户的聊天记录的拉取
     * */
     List<BlogChattingRecords>  singleChatList(@Param("userId")Long userId,@Param("sendUserId")Long sendUserId);

     /*
     * 刚插入的聊天记录详细信息
     * */
     BlogChattingRecords inserSingleChat(BlogChattingRecords blogChattingRecords);
}
