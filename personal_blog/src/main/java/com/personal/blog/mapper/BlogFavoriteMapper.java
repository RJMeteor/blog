package com.personal.blog.mapper;

import com.personal.blog.cover.BlogFavoriteCover;
import com.personal.blog.pojo.BlogFavorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.personal.blog.utils.validators.annotation.Phone;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 收藏夹 Mapper 接口
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface BlogFavoriteMapper extends BaseMapper<BlogFavorite> {
    /*
    * 用户的收藏总数
    * */
    Long userIdAscount(@Param("id")Long id);


    /*
    * 用户对应的文件夹的收藏列表
    * */
    List<BlogFavorite> favoriteNameList(@Param("userId")Long userId,
                                        @Param("favoriteName")String favoriteName,
                                        @Param("page")Integer page,
                                        @Param("pageSize")Integer pageSize);


    /*
    * 统计每个收藏夹的文章个数
    * */
    List<BlogFavoriteCover> favoriteNameAsCount(@Param("userId")Long userId);

    /*
     * 收藏夹名字列表
     * */
    List<String> favoriteNameList();

}
