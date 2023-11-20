package com.personal.blog.controller;


import com.personal.blog.cover.EventPrivateLetter;
import com.personal.blog.pojo.BlogChattingRecords;
import com.personal.blog.service.IBlogChattingRecordsService;
import com.personal.blog.utils.R;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 聊天记录 前端控制器
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Controller
@RequestMapping("/blogChattingRecords")
public class BlogChattingRecordsController {
    @Resource
    private IBlogChattingRecordsService iBlogChattingRecordsService;

    @GetMapping("chatListAsActive")
    @ResponseBody
    public R<List<EventPrivateLetter>> chatListAsActive(@RequestParam("userId") Long userId) {
        return iBlogChattingRecordsService.chatListAsActive(userId);
    }

    @PostMapping("updateChatListAsStatus")
    @ResponseBody
    public R<BlogChattingRecords> updateChatListAsStatus(@RequestParam("acceptorUserId") Long acceptorUserId,
                                       @RequestParam("senderUserId") Long senderUserId) {
        iBlogChattingRecordsService.updateChatListAsStatus(acceptorUserId, senderUserId);
        R<BlogChattingRecords> objectR = new R<>();
        objectR.success(new BlogChattingRecords() );
        return objectR;
    }

    @PostMapping("updatePrivateletterAllAsStatus")
    @ResponseBody
    public R<String> updatePrivateletterAllAsStatus(@RequestBody List<Long> privateId) {
        return iBlogChattingRecordsService.updatePrivateletterAllAsStatus(privateId);
    }

    @DeleteMapping("deletePrivateletterAllAsActive")
    @ResponseBody
    public R<String> deletePrivateletterAllAsActive(@RequestBody List<Long> privateId) {
        return iBlogChattingRecordsService.deletePrivateletterAllAsActive(privateId);
    }

    @GetMapping("singleChatList")
    @ResponseBody
    public R<List<BlogChattingRecords>> singleChatList(@RequestParam("acceptorUserId") Long acceptorUserId,
                                                       @RequestParam("senderUserId") Long senderUserId) {
        return iBlogChattingRecordsService.singleChatList(acceptorUserId, senderUserId);
    }

    @DeleteMapping("deleteChatListAsActive")
    @ResponseBody
    public R<String> deleteChatListAsActive(@RequestParam("chatid") Long chatid) {
        return iBlogChattingRecordsService.deleteChatListAsActive(chatid);
    }

}

