import request from '../../config/axios.config'
import {Login, Regist} from "../../define/login.request";
import {Article, Articles} from "../../define/creation.request";
import {R} from "../../define/r";
import {Comment} from "../../define/messagecenter";
import {Pagination} from "../../define/pagination";

//上传图片
function uploadImg(data: FormData): Promise<R<string>> {
    return request({
        method: "POST",
        url: 'blogArticle/uploadImg',
        data,
        headers: {
            "Content-Type": "multipart/form-data"
        }
    })
}

//发布文章
function seveArticle(data: Article): Promise<R<string>> {
    return request({
        method: "put",
        url: 'blogArticle/seveArticle',
        data,
        headers: {
            "Content-Type": "application/json"
        }
    })
}

/*
* 用户的文章列表
* */
function articleAsUserId(userId: number,page:number, size:number): Promise<R<Pagination<Articles>>> {
    return request({
        method: "get",
        url: 'blogArticle/articleAsUserId',
        params: {
            userId,
            page,
            size
        }
    })
}

/*
* 所有文章列表
* */
function articles(page:number,size:number,articlesName?:string): Promise<R<Pagination<Articles>>> {
    return request({
        method: "get",
        url: 'blogArticle/articles',
        params:{
            page,
            size,
            articlesName
        }
    })
}

//阅读文章
function readArticle(articleId: number, readerUserId: number): Promise<R<Articles>> {
    return request({
        method: "get",
        url: 'blogArticle/readArticle',
        params: {
            articleId,
            readerUserId
        }
    })
}


//删除文章
function deleteArticles(articleId: number, userId: number): Promise<R<string>> {
    return request({
        method: "delete",
        url: 'blogArticle/deleteArticle',
        params: {
            articleId,
            userId
        }
    })
}

//编辑文章
function editArticle(article: Articles | undefined): Promise<R<string>> {
    return request({
        method: "POST",
        url: 'blogArticle/editArticle',
        data: article
    })
}

//评论文章
function sendComment(data: Comment): Promise<R<Comment>> {
    return request({
        method: "put",
        url: 'blogComment/sendComment',
        data
    })
}




//文章的评论
function getComment(articleId: number): Promise<R<Array<Comment>>> {
    return request({
        method: "get",
        url: 'blogComment/getComment',
        params: {
            articleId
        }
    })
}

//文章的添加浏览记录
function doBrowseAsArticle(like: {
    userId: number;
    personId: number;
    articleId: number;
}): Promise<R<any>> {
    return request({
        method: "put",
        url: 'blogLike/doBrowseAsArticle',
        data:like
    })
}


export {
    uploadImg,
    seveArticle,
    articles,
    articleAsUserId,
    readArticle,
    deleteArticles,
    editArticle,
    sendComment,
    getComment,
    doBrowseAsArticle
}