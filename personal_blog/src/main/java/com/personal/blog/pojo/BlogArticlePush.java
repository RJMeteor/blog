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
 * 文章推送
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Getter
@Setter
@TableName("blog_article_push")
public class BlogArticlePush implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 接收用户id
     */
    private Long acceptUserId;

    /**
     * 发送者用户id
     */
    private Long sendUserId;

    /**
     * 0:未读，1:已读
     */
    private Integer readeStatus;

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
    private BlogArticle article;

    @TableField(exist = false)
    private BlogUser acceptUser;

    @TableField(exist = false)
    private BlogUser sendUser;
}
