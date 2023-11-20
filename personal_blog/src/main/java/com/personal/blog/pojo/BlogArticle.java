package com.personal.blog.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.personal.blog.cover.BlogUserCover;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 文章
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Getter
@Setter
@TableName("blog_article")
public class BlogArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章内容ID
     */
    private Long articleContentMappingId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 0:未删除，1:已删除
     */
    private Integer deleted;

    @TableField(exist = false)
    private BlogArticleContentMapping blogArticleContentMapping;

    @TableField(exist = false)
    private BlogUser blogUser;

    @TableField(exist = false)
    private Long like;

    @TableField(exist = false)
    private Long browse;
    @TableField(exist = false)
    private BlogUserCover blogUserCover;


    @TableField(exist = false)
    private List<BlogComment> blogCommentList;


    @TableField(exist = false)
    private List<String> favoriteNameList;

    /*
     * 阅读时候用
     * */
    /*
     * 该文章是否被收藏
     * */
    @TableField(exist = false)
    private Boolean isFavorite;
    /*
     * 该文章是否被点赞
     * */
    @TableField(exist = false)
    private Boolean isLike;

    /*
     * 该文章的评论数
     * */
    @TableField(exist = false)
    private Long commentCount;
}
