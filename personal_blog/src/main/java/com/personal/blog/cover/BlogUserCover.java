package com.personal.blog.cover;

import com.baomidou.mybatisplus.annotation.TableField;
import com.personal.blog.pojo.BlogArt;
import com.personal.blog.pojo.BlogUser;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BlogUserCover {

    /*
     * 关注
     * */
    private Long attention;

    private List<Long> attentionAsId;

    /**
     * 粉丝
     */
    private Long fans;

    /**
     * 浏览
     */
    private Long browse;

    /**
     * 文章
     */
    private Long article;

    /**
     * 收藏
     */
    private Long favorite;

    /**
     * 点赞
     */
    private Long praise;

    /**
     * 用户
     */
    private BlogUser blogUser;

    /*
    * 技术领域列表
    * */
    private List<BlogArt> blogArtList;

}
