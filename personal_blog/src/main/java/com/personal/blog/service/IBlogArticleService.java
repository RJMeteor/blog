package com.personal.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personal.blog.cover.BlogArticleCover;
import com.personal.blog.pojo.BlogArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.blog.utils.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 文章 服务类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface IBlogArticleService extends IService<BlogArticle> {

    R uploadImg(List<MultipartFile> file);

    R<String> seveArticle(BlogArticleCover article);

    R<Page<BlogArticle>> articleList(Long userId,Integer page, Integer size);

    R<Page<BlogArticle>> articles(Integer page, Integer size,String name);

    R<BlogArticle> readArticle(Long articleId,Long readerUserId);

    R<String> deleteArticle(Long articleId, Long userId);

    R<String> editArticle(BlogArticle blogArticle);

    Long articleToId(BlogArticle blogArticle);
}
