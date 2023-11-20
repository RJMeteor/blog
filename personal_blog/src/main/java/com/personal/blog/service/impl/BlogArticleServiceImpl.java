package com.personal.blog.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personal.blog.cover.BlogArticleCover;
import com.personal.blog.cover.EventArticlepush;
import com.personal.blog.cover.coverImpe.EventCover;
import com.personal.blog.mapper.BlogArticleContentMappingMapper;
import com.personal.blog.mapper.BlogArticlePushMapper;
import com.personal.blog.pojo.*;
import com.personal.blog.mapper.BlogArticleMapper;
import com.personal.blog.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.blog.utils.EventMetadata;
import com.personal.blog.utils.R;
import com.personal.blog.utils.SseEmitterUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 文章 服务实现类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Service
public class BlogArticleServiceImpl extends ServiceImpl<BlogArticleMapper, BlogArticle> implements IBlogArticleService {

    @Resource
    private BlogArticleContentMappingMapper blogArticleContentMappingMapper;
    @Resource
    private BlogArticleMapper blogArticleMapper;
    @Resource
    private IBlogAttentionService iBlogAttentionService;
    @Resource
    private EventCover eventCover;
    @Resource
    private IBlogArticlePushService iBlogArticlePushService;
    @Resource
    private BlogArticlePushMapper blogArticlePushMapper;
    @Resource
    private IBlogCommentService iBlogCommentService;
    @Resource
    private IBlogLikeService iBlogLikeService;

    @Resource
    private IBlogFavoriteService iBlogFavoriteService;

    @Override
    public R uploadImg(List<MultipartFile> file) {
        for (MultipartFile multipartFile : file) {
            System.out.println(multipartFile.getOriginalFilename());
        }
        return null;
    }

    @Override
    public R<String> seveArticle(BlogArticleCover article) {
        BlogArticleContentMapping blogArticleContentMapping = new BlogArticleContentMapping();
        blogArticleContentMapping.setArticleContent(article.getArticleContent());
        blogArticleContentMapping.setArticleContentNotHtml(article.getArticleContentNotHtml());
        blogArticleContentMapping.setMdContent(article.getMdContent());
        blogArticleContentMappingMapper.saveContent(blogArticleContentMapping);

        BlogArticle blogArticle = new BlogArticle();
        blogArticle.setArticleTitle(article.getArticleTitle());
        blogArticle.setArticleContentMappingId(blogArticleContentMapping.getId());
        blogArticle.setUserId(article.getUserId());
        int insert = this.baseMapper.insert(blogArticle);

        R<String> r = new R<String>();
        if (insert > 0) {
            r.success("发布成功");

            LambdaQueryWrapper<BlogAttention> blogAttentionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            blogAttentionLambdaQueryWrapper.eq(BlogAttention::getAttentionUserId, article.getUserId())
                    .select(BlogAttention::getUserId);

            Long blogArticleId = this.articleToId(blogArticle);

            List<Object> objects = iBlogAttentionService.listObjs(blogAttentionLambdaQueryWrapper);
            for (Object object : objects) {
                BlogArticlePush blogArticlePush = new BlogArticlePush();
                blogArticlePush.setSendUserId(blogArticle.getUserId());
                blogArticlePush.setAcceptUserId(Long.valueOf(String.valueOf(object)));
                blogArticlePush.setArticleId(blogArticleId);

                iBlogArticlePushService.save(blogArticlePush);

                BlogArticlePush blogArticlePush1 = blogArticlePushMapper.addArtcleAsPush(blogArticlePush);
                SseEmitterUtil sseEmitterUtil = SseEmitterUtil.sses.get(Long.valueOf(String.valueOf(object)));
                System.out.println(sseEmitterUtil);
                if (!ObjectUtils.isEmpty(sseEmitterUtil)) {
                    EventArticlepush eventArticlepush = eventCover.blogArticlePushToEventArticlepush(blogArticlePush1);
                    try {
                        sseEmitterUtil.send(EventMetadata.Articlepush, JSONObject.toJSONString(eventArticlepush));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            r.error("发布失败");
        }

        return r;
    }

    @Override
    public R<Page<BlogArticle>> articleList(Long userId,Integer page, Integer size) {
        LambdaQueryWrapper<BlogArticle> blogArticleLambdaQueryWrapper = new LambdaQueryWrapper<BlogArticle>()
                .eq(BlogArticle::getUserId,userId)
                .orderByDesc(BlogArticle::getCreateTime);
        Page<BlogArticle> blogArticlePage = new Page<>(page,size);
        Page<BlogArticle> page1 = this.page(blogArticlePage, blogArticleLambdaQueryWrapper);
        List<BlogArticle> blogArticles = blogArticleMapper.articleList(userId,page * size - size, size);
        page1.setRecords(blogArticles);
        R<Page<BlogArticle>> r = new R<Page<BlogArticle>>();
        r.success(page1);
        return r;
    }

    @Override
    public R<Page<BlogArticle>> articles(Integer page, Integer size,String name) {
        LambdaQueryWrapper<BlogArticle> blogArticleLambdaQueryWrapper = new LambdaQueryWrapper<BlogArticle>()
                .orderByDesc(BlogArticle::getCreateTime);
        if (!ObjectUtils.isEmpty(name)){
            blogArticleLambdaQueryWrapper.like(BlogArticle::getArticleTitle,name);
        }

        Page<BlogArticle> blogArticlePage = new Page<>(page,size);
        Page<BlogArticle> page1 = this.page(blogArticlePage, blogArticleLambdaQueryWrapper);
        List<BlogArticle> articles = blogArticleMapper.articles(page * size - size, size,!ObjectUtils.isEmpty(name)?name:"");
        System.out.println(articles.size());
        page1.setRecords(articles);
        R<Page<BlogArticle>> listR = new R<>();
        listR.success(page1);
        return listR;
    }

    @Override
    public R<BlogArticle> readArticle(Long articleId, Long readerUserId) {
        BlogArticle blogArticle = this.baseMapper.singleArticle(articleId);
        if (!ObjectUtils.isEmpty(blogArticle)) {
            LambdaQueryWrapper<BlogFavorite> favorite = new LambdaQueryWrapper<BlogFavorite>()
                    .eq(BlogFavorite::getArticleId, articleId)
                    .eq(BlogFavorite::getUserId, readerUserId);
            LambdaQueryWrapper<BlogLike> like = new LambdaQueryWrapper<BlogLike>()
                    .eq(BlogLike::getArticleId, articleId)
                    .eq(BlogLike::getUserId, readerUserId)
                    .eq(BlogLike::getLikeBrowseLimiter,0);
            blogArticle.setIsFavorite(!ObjectUtils.isEmpty(iBlogFavoriteService.listObjs(favorite)));
            blogArticle.setCommentCount(Long.valueOf(blogArticle.getBlogCommentList().size()));
            blogArticle.setIsLike(!ObjectUtils.isEmpty(iBlogLikeService.listObjs(like)));
        }
        List<BlogComment> blogComments = iBlogCommentService.processMultiLevelComments(blogArticle.getBlogCommentList());
        blogArticle.setBlogCommentList(blogComments);
        R<BlogArticle> blogArticleR = new R<>();
        blogArticleR.success(blogArticle);
        return blogArticleR;
    }

    @Override
    public R<String> deleteArticle(Long articleId, Long userId) {
        LambdaQueryWrapper<BlogArticle> blogArticleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogArticleLambdaQueryWrapper.eq(BlogArticle::getId, articleId)
                .eq(BlogArticle::getUserId, userId);
        boolean remove = this.remove(blogArticleLambdaQueryWrapper);

        LambdaQueryWrapper<BlogFavorite> favorite = new LambdaQueryWrapper<BlogFavorite>()
                .eq(BlogFavorite::getArticleId, articleId);
        boolean remove1 = iBlogFavoriteService.remove(favorite);

        LambdaQueryWrapper<BlogArticlePush> articlePush = new LambdaQueryWrapper<BlogArticlePush>()
                .eq(BlogArticlePush::getArticleId, articleId);
        boolean remove3 = iBlogArticlePushService.remove(articlePush);

        LambdaQueryWrapper<BlogLike> like = new LambdaQueryWrapper<BlogLike>()
                .eq(BlogLike::getArticleId, articleId);
        boolean remove2 = iBlogLikeService.remove(like);
        R<String> stringR = new R<>();
        if (remove) {
            stringR.success("删除成功");
        } else {
            stringR.error("删除失败");
        }

        return stringR;
    }

    @Override
    public R<String> editArticle(BlogArticle blogArticle) {
        blogArticle.getBlogArticleContentMapping().setId(blogArticle.getArticleContentMappingId());
        int i = blogArticleContentMappingMapper.updateById(blogArticle.getBlogArticleContentMapping());
        R<String> blogArticleR = new R<>();
        if (i > 0) {
            BlogArticle blogArticle1 = new BlogArticle();
            blogArticle1.setId(blogArticle.getId());
            blogArticle1.setUserId(blogArticle.getUserId());
            blogArticle1.setArticleTitle(blogArticle.getArticleTitle());
            blogArticle1.setArticleContentMappingId(blogArticle.getArticleContentMappingId());
            boolean b = this.updateById(blogArticle1);
            if (b) {
                blogArticleR.success("编辑成功");
            } else {
                blogArticleR.error("编辑失败");
            }
        } else {
            blogArticleR.error("编辑失败");
        }
        return blogArticleR;
    }

    @Override
    public Long articleToId(BlogArticle blogArticle) {
        return blogArticleMapper.articleToId(blogArticle);
    }
}
