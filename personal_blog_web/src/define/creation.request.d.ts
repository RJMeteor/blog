import {Time} from "./Time";
import {User, UserInfo} from "./user";
import {Comment} from "./messagecenter";

type ArticleContentMapping = {
    /**
     * 文章内容
     */
    id?: number
    /**
     * 文章内容
     */
    articleContent?: string

    /*
     * 文章文章无html
     * */
    articleContentNotHtml?: string;

    /*
    * md
    * */
    mdContent?: string;
    article?: Articles;
}

type Article = ArticleContentMapping & Time & {

    /*
    * 文章ID
    * */
    id?: number

    /**
     * 用户id
     */
    userId?: number;

    /**
     * 文章标题
     */
    articleTitle?: string

    mdContent?: string,

};

type Articles = Time & {
    /*
    * 文章ID
    * */
    id?: number
    /**
     * 用户id
     */
    userId?: number;

    /**
     * 文章标题
     */
    articleTitle?: string
    blogUser?: User;
    blogUserCover?: UserInfo;
    /*
    * 点赞数
    * */
    like?: number;
    /*
    * 浏览数
    * */
    browse?: number;
    /*
    * 文章内容映射
    * */
    articleContentMappingId?:number;
    /*
    * 文章评论列表映射
    * */
    blogArticleContentMapping?: ArticleContentMapping,
    /*
    * 文章评论列表
    * */
    blogCommentList?:Array<Comment>
    /*
    * 收藏夹名字列表
    * */
    favoriteNameList?:Array<string>

    /*
    * 阅读时候用
    * */
    /*
    * 该文章是否被收藏
    * */
    isFavorite:boolean,
    /*
    * 该文章是否被点赞
    * */
    isLike:boolean,

    /*
    * 该文章的评论数
    * */
    commentCount:number

};


export {
    Article,
    Articles
};