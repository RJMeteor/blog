package com.personal.blog.controller;


import com.personal.blog.cover.EventComment;
import com.personal.blog.pojo.BlogComment;
import com.personal.blog.service.IBlogCommentService;
import com.personal.blog.utils.R;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.xml.stream.events.Comment;
import java.util.List;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Controller
@RequestMapping("/blogComment")
public class BlogCommentController {
    @Resource
    private IBlogCommentService iBlogCommentService;

    /*
    * 推送在列表里的评论
    * */
    @GetMapping("commentListAsActive")
    @ResponseBody
    public R<List<EventComment>> commentListAsActive(@RequestParam("userId") Long userId) {
        return iBlogCommentService.commentListAsActive(userId);
    }


    /*
     * 把推送评论消息为已读
     * * */
    @PostMapping("updateCommentListAsStatus")
    @ResponseBody
    public  R<BlogComment>  updateCommentListAsStatus(@RequestParam("commentId") Long commentId) {
         iBlogCommentService.updateCommentListAsStatus(commentId);
        R<BlogComment> objectR = new R<>();
        objectR.success(new BlogComment());
        return objectR;
    }

    /*
    * 把推送消息从列表里剔除
    * */
    @DeleteMapping("deleteCommentListAsActive")
    @ResponseBody
    public R<String> deleteCommentListAsActive(@RequestParam("commentId") Long commentId) {
        return iBlogCommentService.deleteCommentListAsActive(commentId);
    }

    @DeleteMapping("deleteCommentAllAsActive")
    @ResponseBody
    public R<String> deleteCommentAllAsActive(@RequestParam("userId") Long userId) {
        return iBlogCommentService.deleteCommentAllAsActive(userId);
    }

    @PostMapping("updateCommentAllAsStatus")
    @ResponseBody
    public R<String> updateCommentAllAsStatus(@RequestParam("userId") Long userId) {
        return iBlogCommentService.updateCommentAllAsStatus(userId);
    }

    /*
     * 添加评论
     * */
    @PutMapping("sendComment")
    @ResponseBody
    public R sendComment(@RequestBody BlogComment blogComment) {
        return iBlogCommentService.sendComment(blogComment);
    }

    /*
     * 添加评论
     * */
    @PutMapping("sendCommentAsPushList")
    @ResponseBody
    public R<String> sendCommentAsPushList(@RequestBody BlogComment blogComment) {
        return iBlogCommentService.sendCommentAsPushList(blogComment);
    }
    /*
     * 添加评论
     * */
    @GetMapping("getComment")
    @ResponseBody
    public R<List<BlogComment>> getComment(@RequestParam("articleId")Long articleId) {
        return iBlogCommentService.getComment(articleId);
    }

}

