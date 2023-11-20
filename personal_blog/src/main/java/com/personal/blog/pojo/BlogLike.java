package com.personal.blog.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 点赞
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Getter
@Setter
@TableName("blog_like_browse")
public class BlogLike implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 点赞/浏览的用户id
     */
    private Long personId;
    /**
     * 文章id
     */
    private Long articleId;

    /*
    * 是否已读
    * */
    private Integer stated;

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

    /**
     * 0:点赞，1:浏览
     * */
    private Integer likeBrowseLimiter;

    /**
     *
     * 0:在推送列表里，1：不在
     */
    private Integer active;

    @TableField(exist = false)
    private BlogUser user;
    @TableField(exist = false)
    private BlogUser personUser;


    @TableField(exist = false)
    private BlogArticle article;
}
