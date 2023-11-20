package com.personal.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 文章内容映射
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Getter
@Setter
@TableName("blog_article_content_mapping")
public class BlogArticleContentMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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

    @TableField(exist = false)
    private BlogArticle article;

}
