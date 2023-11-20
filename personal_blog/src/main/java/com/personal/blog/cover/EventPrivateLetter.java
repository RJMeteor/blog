package com.personal.blog.cover;

import com.baomidou.mybatisplus.annotation.TableField;
import com.personal.blog.pojo.BlogArticle;
import com.personal.blog.pojo.BlogUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventPrivateLetter {
    private Long privateleId;
    private BlogUser sendUser;
    private BlogUser acceptorUser;
    private String comment;
    private String createTime;
    private Integer readeStatus;
}
