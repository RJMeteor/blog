package com.personal.blog.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personal.blog.cover.BlogFavoriteCover;
import com.personal.blog.pojo.BlogFavorite;
import com.personal.blog.service.IBlogFavoriteService;
import com.personal.blog.utils.R;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 收藏夹 前端控制器
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Controller
@RequestMapping("/blogFavorite")
public class BlogFavoriteController {
    @Resource
    private IBlogFavoriteService iBlogFavoriteService;

    @GetMapping("favoriteNameAsCount")
    @ResponseBody
    public R<List<BlogFavoriteCover>> favoriteNameAsCount(@RequestParam("userId") Long userId) {
        return iBlogFavoriteService.favoriteNameAsCount(userId);
    }


    @GetMapping("favoriteNameList")
    @ResponseBody
    public R<Page<BlogFavorite>> favoriteNameList(@RequestParam("userId") Long userId,
                                                  @RequestParam("favoriteName") String favoriteName,
                                                  @RequestParam("page") Integer page,
                                                  @RequestParam("pageSize") Integer pageSize) {
        return iBlogFavoriteService.favoriteNameList(userId, favoriteName, page, pageSize);
    }

    @PutMapping("addFavorite")
    @ResponseBody
    public R<String> addFavorite(@RequestParam("userId") Long userId, @RequestParam("favoriteName") String favoriteName) {
        return iBlogFavoriteService.addFavorite(userId, favoriteName);
    }

    @PutMapping("favoriteAsArticle")
    @ResponseBody
    public R<String> favoriteAsArticle(@RequestParam("userId") Long userId,
                                       @RequestParam("favoriteName") String favoriteName,
                                       @RequestParam("articleId") Long articleId) {
        return iBlogFavoriteService.favoriteAsArticle(userId, favoriteName, articleId);
    }

    @DeleteMapping("deleteInFavoriteAsArticle")
    @ResponseBody
    public R<String> deleteInFavoriteAsArticle(@RequestBody List<Integer> list) {
        return iBlogFavoriteService.deleteInFavoriteAsArticle(list);
    }

    @DeleteMapping("deleteFavorite")
    @ResponseBody
    public R<String> deleteFavorite(@RequestParam("userId") Long userId, @RequestParam("favoriteName") String favoriteName) {
        return iBlogFavoriteService.deleteFavorite(userId, favoriteName);
    }

    @PostMapping("editFavorite")
    @ResponseBody
    public R<String> editFavorite(@RequestParam("userId") Long userId,
                                  @RequestParam("oldFavoriteName") String oldFavoriteName,
                                  @RequestParam("newFavoriteName") String newFavoriteName) {
        return iBlogFavoriteService.editFavorite(userId, oldFavoriteName, newFavoriteName);
    }

    /*
     * 收藏文章
     * */
    @PutMapping("addFavoriteAsArticle")
    @ResponseBody
    public R<String> addFavoriteAsArticle(@RequestBody BlogFavorite blogFavorite) {
        return iBlogFavoriteService.addFavoriteAsArticle(blogFavorite);
    }

    /*
     * 删除收藏的文章
     * */
    @DeleteMapping("deleteFavoriteAsArticle")
    @ResponseBody
    public R<String> deleteFavoriteAsArticle(@RequestParam("userId") Long userId,
                                             @RequestParam("articleId") Long articleId) {
        return iBlogFavoriteService.deleteFavoriteAsArticle(userId, articleId);
    }

}

