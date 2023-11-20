package com.personal.blog.cover;

import com.personal.blog.pojo.BlogArticle;
import com.personal.blog.pojo.BlogUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventArticlepush {
    private Long articlepushId;
    private BlogUser acceptUser;
    private BlogUser sendUser;
    private BlogArticle article;
    private String comment;
    private String createTime;
    private Integer readeStatus;
}
