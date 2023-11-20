package com.personal.blog.cover;

import com.personal.blog.pojo.BlogUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventAttention {
    private Long attentionId;
    private BlogUser attentionUser;
    private String createTime;
    private Integer stated;
}
