package com.personal.blog.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 收藏夹
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Getter
@Setter
@ToString
@TableName("blog_favorite")
public class BlogFavorite implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收藏夹的名字
     */
    private String favoriteName;

    /**
     * 文章id
     */
    private Long articleId;

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
    private BlogUser user;

    @TableField(exist = false)
    private BlogArticle article;
}
