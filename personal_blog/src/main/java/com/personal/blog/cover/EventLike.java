package com.personal.blog.cover;

import com.personal.blog.pojo.BlogArticle;
import com.personal.blog.pojo.BlogUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventLike {
    private Long likeId;
    private BlogUser sendUser;
    private BlogArticle article;
    private String createTime;
    private Integer stated;
}
