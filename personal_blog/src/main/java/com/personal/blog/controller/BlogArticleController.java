package com.personal.blog.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personal.blog.cover.BlogArticleCover;
import com.personal.blog.pojo.BlogArticle;
import com.personal.blog.service.IBlogArticleService;
import com.personal.blog.utils.R;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 文章 前端控制器
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Controller
@RequestMapping("/blogArticle")
public class BlogArticleController {
    @Resource
    private IBlogArticleService iBlogArticleService;

    @PostMapping("uploadImg")
    @ResponseBody
    public R uploadImg(@RequestParam("file") List<MultipartFile> file) {
        return iBlogArticleService.uploadImg(file);
    }

    @PutMapping("seveArticle")
    @ResponseBody
    public R<String> seveArticle(@RequestBody BlogArticleCover article) {
        return iBlogArticleService.seveArticle(article);
    }

    @GetMapping("articleAsUserId")
    @ResponseBody
    public R<Page<BlogArticle>> articleAsUserId(@RequestParam("userId") Long userId,@RequestParam("page")Integer page, @RequestParam("size")Integer size) {
        return iBlogArticleService.articleList(userId,page,size);
    }

    @GetMapping("articles")
    @ResponseBody
    public R<Page<BlogArticle>> articles(@RequestParam("page")Integer page,
                                         @RequestParam("size")Integer size,
                                         @RequestParam(value = "articlesName",required = false)String name) {
        return iBlogArticleService.articles(page,size,name);
    }

    @GetMapping("readArticle")
    @ResponseBody
    public R<BlogArticle> readArticle(@RequestParam("articleId") Long articleId,@RequestParam("readerUserId") Long readerUserId) {
        return iBlogArticleService.readArticle(articleId,readerUserId);
    }

    @DeleteMapping("deleteArticle")
    @ResponseBody
    public R<String> deleteArticle(@RequestParam("articleId") Long articleId,@RequestParam("userId") Long userId) {
        return iBlogArticleService.deleteArticle(articleId,userId);
    }

    @PostMapping("editArticle")
    @ResponseBody
    public R<String> editArticle(@RequestBody BlogArticle blogArticle) {
        return iBlogArticleService.editArticle(blogArticle);
    }
}

