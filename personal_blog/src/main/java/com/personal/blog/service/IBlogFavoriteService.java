package com.personal.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personal.blog.cover.BlogFavoriteCover;
import com.personal.blog.pojo.BlogFavorite;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.blog.utils.R;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 收藏夹 服务类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface IBlogFavoriteService extends IService<BlogFavorite> {
    R<List<BlogFavoriteCover>> favoriteNameAsCount(Long userId);

    R<Page<BlogFavorite>> favoriteNameList(Long userId, String favoriteName, Integer page, Integer pageSize);

    R<String> addFavorite(Long userId, String favoriteName);

    R<String> favoriteAsArticle( Long userId,String favoriteName,Long articleId);

    R<String> deleteInFavoriteAsArticle(List<Integer> list);

    R<String> deleteFavorite( Long userId,String favoriteName);

    R<String> editFavorite( Long userId,String oldFavoriteName,String newFavoriteName);

    R<String> addFavoriteAsArticle(BlogFavorite blogFavorite);

    R<String> deleteFavoriteAsArticle( Long userId, Long articleId);
}
