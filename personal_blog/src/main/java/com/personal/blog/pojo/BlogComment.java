package com.personal.blog.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 评论
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Getter
@Setter
@TableName("blog_comment")
@ToString
public class BlogComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 评论的id
     * */
    private Long commentKeyId ;

    /*
    * 父级评论的用户Id
    * */
    private Long personId;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     *
     * 0:在推送列表里，1：不在
     */
    private Integer active;

    /*
     * 是否已读
     * */
    private Integer stated;

    /**
     * 评论id
     */
    private Long commentContentMappingId;

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


    private Integer deleted;

    @TableField(exist = false)
    private BlogUser user;

    @TableField(exist = false)
    private BlogUser personUser;

    @TableField(exist = false)
    private BlogArticle article;


    @TableField(exist = false)
    private BlogCommentContentMapping commentContentMapping;

    /*
    * 后期优化
    * 评论是否有子元素，允许懒加载
    * */
    @TableField(exist = false)
    private Boolean isChildren;

    /*
    * 后期优化
    * 评论子元素
    * */
    @TableField(exist = false)
    private List<BlogComment> children;
}
