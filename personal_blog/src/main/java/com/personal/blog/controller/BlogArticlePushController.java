package com.personal.blog.controller;


import com.personal.blog.cover.EventArticlepush;
import com.personal.blog.mapper.BlogArticleMapper;
import com.personal.blog.pojo.BlogArticlePush;
import com.personal.blog.service.IBlogArticlePushService;
import com.personal.blog.utils.R;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 文章推送 前端控制器
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Controller
@RequestMapping("/blogArticlePush")
public class BlogArticlePushController {
    @Resource
    private IBlogArticlePushService iBlogArticlePushService;

    @GetMapping("artclePush")
    @ResponseBody
    public R<List<EventArticlepush>> artclePush(@RequestParam("userId") Long userid) {
        return iBlogArticlePushService.artclePush(userid);
    }

    @DeleteMapping("deleteArticlePush")
    @ResponseBody
    public R<String> deleteArticlePush(@RequestParam("articlePushId") Long articlePushId) {
        return iBlogArticlePushService.deleteArticlePush(articlePushId);
    }

    @PostMapping("updateArticlePushAllAsStatus")
    @ResponseBody
        public R<String> updateArticlePushAllAsStatus(@RequestParam("userId") Long userId) {
        return iBlogArticlePushService.updateArticlePushAllAsStatus(userId);
    }

    @DeleteMapping("deleteArticlePushAllAsActive")
    @ResponseBody
    public R<String> deleteArticlePushAllAsActive(@RequestParam("userId") Long userId) {
        return iBlogArticlePushService.deleteArticlePushAllAsActive(userId);
    }

}

