package com.personal.blog.controller;


import com.personal.blog.cover.EventLike;
import com.personal.blog.pojo.BlogLike;
import com.personal.blog.service.IBlogLikeService;
import com.personal.blog.utils.R;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 点赞 前端控制器
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Controller
@RequestMapping("/blogLike")
public class BlogLikeController {

    @Resource
    private IBlogLikeService iBlogLikeService;


//    @GetMapping("likeList")
//    @ResponseBody
//   public R<List<BlogLike>> likeList(@Param("id")Long id){
//       return  iBlogLikeService.likeList(id);
//   }


    /*
     * 拉取在推送列表里的
     * */
    @GetMapping("likeListAsActive")
    @ResponseBody
    public R<List<EventLike>> likeListAsActive(@RequestParam("userId") Long userId) {
        return iBlogLikeService.likeListAsActive(userId);
    }

    /*
     * 从推送列表里剔除
     * */
    @DeleteMapping("deleteLikeListAsActive")
    @ResponseBody
    public R<String> deleteLikeListAsActive(@RequestParam("likeId") Long likeId) {
        return iBlogLikeService.deleteLikeListAsActive(likeId);
    }

    /*
     * 剔除用户的推送列表
     * */
    @DeleteMapping("deleteLikeAllAsActive")
    @ResponseBody
    public R<String> deleteLikeAllAsActive(@RequestParam("userId") Long userId) {
        return iBlogLikeService.deleteLikeAllAsActive(userId);
    }

    /*
     * 更改用户的推送列表为已读
     * */
    @PostMapping("updateLikeAllAsStatus")
    @ResponseBody
    public R<String> updateLikeAllAsStatus(@RequestParam("userId") Long userId) {
        return iBlogLikeService.updateLikeAllAsStatus(userId);
    }


    @PutMapping("doLikeAsArticle")
    @ResponseBody
    public R<String> doLikeAsArticle(@RequestBody BlogLike blogLike) {
        return iBlogLikeService.doLikeAsArticle(blogLike);
    }

    @PutMapping("doBrowseAsArticle")
    @ResponseBody
    public BlogLike doBrowseAsArticle(@RequestBody BlogLike blogLike) {
        iBlogLikeService.doBrowseAsArticle(blogLike);
        return new BlogLike();
    }

    @DeleteMapping("deleteLikeAsArticle")
    @ResponseBody
    public R<String> deleteLikeAsArticle(@RequestParam("userId") Long userId, @RequestParam("articleId") Long articleId) {
        return iBlogLikeService.deleteLikeAsArticle(userId, articleId);
    }


}

