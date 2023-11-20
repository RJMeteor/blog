package com.personal.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personal.blog.cover.BlogFavoriteCover;
import com.personal.blog.pojo.BlogFavorite;
import com.personal.blog.mapper.BlogFavoriteMapper;
import com.personal.blog.service.IBlogFavoriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.blog.utils.R;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 收藏夹 服务实现类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Service
public class BlogFavoriteServiceImpl extends ServiceImpl<BlogFavoriteMapper, BlogFavorite> implements IBlogFavoriteService {

    @Resource
    private BlogFavoriteMapper blogFavoriteMapper;
    @Resource
    private IBlogFavoriteService iBlogFavoriteService;

    @Override
    public R<List<BlogFavoriteCover>> favoriteNameAsCount(Long userId) {
        List<BlogFavoriteCover> blogFavoriteCovers = blogFavoriteMapper.favoriteNameAsCount(userId);
        R<List<BlogFavoriteCover>> listR = new R<>();
        listR.success(blogFavoriteCovers);
        return listR;
    }

    @Override
    public R<Page<BlogFavorite>> favoriteNameList(Long userId, String favoriteName, Integer page, Integer pageSize) {
        Page<BlogFavorite> objectPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<BlogFavorite> like = new LambdaQueryWrapper<BlogFavorite>()
                .eq(BlogFavorite::getUserId, userId)
                .eq(BlogFavorite::getFavoriteName,favoriteName)
                .orderByDesc(BlogFavorite::getFavoriteName);
        Page<BlogFavorite> page1 = iBlogFavoriteService.page(objectPage, like);
        List<BlogFavorite> blogFavorites = blogFavoriteMapper.favoriteNameList(userId, favoriteName, page * pageSize - pageSize, pageSize);
        Page<BlogFavorite> blogFavoritePage = page1.setRecords(blogFavorites);
        R<Page<BlogFavorite>> listR = new R<>();
        listR.success(blogFavoritePage);
        return listR;
    }

    @Override
    public R<String> addFavorite(Long userId, String favoriteName) {
        R<String> stringR = new R<>();
        LambdaQueryWrapper<BlogFavorite> blogFavoriteLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogFavoriteLambdaQueryWrapper.eq(BlogFavorite::getFavoriteName, favoriteName)
                .eq(BlogFavorite::getUserId, userId);

        BlogFavorite blogFavorite1 = this.baseMapper.selectOne(blogFavoriteLambdaQueryWrapper);
        if (!ObjectUtils.isEmpty(blogFavorite1)) {
            stringR.error("已有该收藏夹");
            return stringR;
        }
        BlogFavorite blogFavorite = new BlogFavorite();
        blogFavorite.setArticleId(0L);
        blogFavorite.setUserId(userId);
        blogFavorite.setFavoriteName(favoriteName);
        int insert = this.baseMapper.insert(blogFavorite);

        if (insert > 0) {
            stringR.success("添加成功");
        } else {
            stringR.error("添加失败");
        }
        return stringR;
    }


    @Override
    public R<String> favoriteAsArticle(Long userId, String favoriteName, Long articleId) {
        BlogFavorite blogFavorite = new BlogFavorite();
        blogFavorite.setUserId(userId);
        blogFavorite.setFavoriteName(favoriteName);
        blogFavorite.setArticleId(articleId);
        int insert = this.baseMapper.insert(blogFavorite);
        R<String> stringR = new R<>();
        if (insert > 0) {
            stringR.success("收藏成功");
        } else {
            stringR.error("收藏失败");
        }
        return stringR;
    }

    @Override
    public R<String> deleteInFavoriteAsArticle(List<Integer> list) {
        boolean b = this.iBlogFavoriteService.removeByIds(list);
        R<String> r = new R<String>();
        if (b){
            r.success("删除成功");
        }else {
            r.error("删除失败");
        }
        return r;
    }

    @Override
    public R<String> deleteFavorite(Long userId, String favoriteName) {
        LambdaQueryWrapper<BlogFavorite> blogFavoriteLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogFavoriteLambdaQueryWrapper.eq(BlogFavorite::getUserId, userId)
                .eq(BlogFavorite::getFavoriteName, favoriteName);
        boolean remove = this.remove(blogFavoriteLambdaQueryWrapper);
        R<String> stringR = new R<>();
        if (remove) {
            stringR.success("删除成功");
        } else {
            stringR.error("删除失败");
        }

        return stringR;
    }

    @Override
    public R<String> editFavorite(Long userId, String oldFavoriteName, String newFavoriteName) {

        LambdaUpdateWrapper<BlogFavorite> blogFavoriteLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        blogFavoriteLambdaUpdateWrapper.eq(BlogFavorite::getUserId, userId)
                .eq(BlogFavorite::getFavoriteName, oldFavoriteName)
                .set(BlogFavorite::getFavoriteName, newFavoriteName);
        boolean remove = this.update(blogFavoriteLambdaUpdateWrapper);
        R<String> stringR = new R<>();
        if (remove) {
            stringR.success("修改成功");
        } else {
            stringR.error("修改失败");
        }
        return stringR;
    }

    @Override
    public R<String> addFavoriteAsArticle(BlogFavorite blogFavorite) {
        R<String> stringR = new R<>();
        LambdaQueryWrapper<BlogFavorite> eq = new LambdaQueryWrapper<BlogFavorite>()
                .eq(BlogFavorite::getUserId, blogFavorite.getUserId())
                .eq(BlogFavorite::getArticleId, blogFavorite.getArticleId());
        List<BlogFavorite> list = this.list(eq);
        if (ObjectUtils.isEmpty(list)) {
            int insert = this.baseMapper.insert(blogFavorite);
            if (insert > 0) {
                stringR.success("收藏成功");
            } else {
                stringR.error("收藏失败");
            }
        } else {
            stringR.success("你已收藏该文章");
        }

        return stringR;
    }

    @Override
    public R<String> deleteFavoriteAsArticle(Long userId, Long articleId) {
        R<String> stringR = new R<String>();
        LambdaQueryWrapper<BlogFavorite> eq = new LambdaQueryWrapper<BlogFavorite>()
                .eq(BlogFavorite::getUserId, userId)
                .eq(BlogFavorite::getArticleId, articleId);
        boolean remove = this.remove(eq);
        if (remove) {
            stringR.success("取消收藏成功");
        } else {
            stringR.error("取消收藏失败");
        }
        return stringR;
    }
}
