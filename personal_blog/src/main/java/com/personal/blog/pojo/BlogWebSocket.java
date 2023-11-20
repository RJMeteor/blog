package com.personal.blog.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BlogWebSocket {
    private Long acceptorUserId;
    private Long senderUserId;
    private String content;
}
