package com.personal.blog.cover;

import com.personal.blog.pojo.BlogArticle;
import com.personal.blog.pojo.BlogUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventComment {
    private Long commentId;
    private BlogUser sendUser;
    private BlogArticle article;
    private String comment;
    private String createTime;
    private Integer stated;
    /**
     * 评论的id
     * */
    private Long commentKeyId;
}
