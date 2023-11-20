package com.personal.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 评论内容映射
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Getter
@Setter
@TableName("blog_comment_content_mapping")
public class BlogCommentContentMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String commentContent;


}
