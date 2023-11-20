package com.personal.blog.cover;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogArticleCover {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章内容
     */
    private String articleContent;

    /*
     * 文章文章无html
     * */
    private String articleContentNotHtml;

    /*
     * md
     * */
    private  String mdContent;


}
