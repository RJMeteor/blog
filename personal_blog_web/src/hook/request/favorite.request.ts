import request from '../../config/axios.config'
import {Login, Regist} from "../../define/login.request";
import {Article, Articles} from "../../define/creation.request";
import {R} from "../../define/r";
import {Favorite, FavoriteNameCount} from "../../define/favorite";
import {Pagination} from "../../define/pagination";


/*
* 每个收藏夹的文件个数
* */
function favoriteNameAsCount(userId: number): Promise<R<Array<FavoriteNameCount>>> {
    return request({
        method: "get",
        url: 'blogFavorite/favoriteNameAsCount',
        params: {
            userId
        }
    })
}


/*
* 用户每个收藏夹的文章列表
* */
function favoriteNameList(userId: number, favoriteName: string, page: number, pageSize: number): Promise<R<Pagination<Favorite>>> {
    return request({
        method: "get",
        url: 'blogFavorite/favoriteNameList',
        params: {
            userId,
            favoriteName,
            page, pageSize
        }
    })
}

/*
* 添加收藏夹
* */
function addFavorite(userId: number, favoriteName: string): Promise<R<Array<Favorite>>> {
    return request({
        method: "put",
        url: 'blogFavorite/addFavorite',
        params: {
            userId,
            favoriteName
        },
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    })
}


/*
* 收藏文章
* */
function favoriteAsArticle(userId: number, favoriteName: string, articleId: number): Promise<R<Array<Favorite>>> {
    return request({
        method: "put",
        url: 'blogFavorite/favoriteAsArticle',
        params: {
            userId,
            favoriteName,
            articleId
        },
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    })
}

function deleteInFavoriteAsArticle(data: number[]): Promise<R<string>> {
    return request({
        method: "delete",
        url: 'blogFavorite/deleteInFavoriteAsArticle',
        data,
        headers: {
            "Content-Type": "application/json"
        }
    })
}


/*
* 删除文件夹
* */
function deleteAsFavorite(favoriteName: string, userId: number): Promise<R<string>> {
    return request({
        method: "delete",
        url: 'blogFavorite/deleteFavorite',
        params: {
            userId,
            favoriteName,
        },
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    })
}


/*
* 修改文件夹名字
* */
function editAsFavorite(oldFavoriteName: string, newFavoriteName: string, userId: number): Promise<R<string>> {
    return request({
        method: "post",
        url: 'blogFavorite/editFavorite',
        data: {
            userId,
            oldFavoriteName,
            newFavoriteName,
        },
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    })
}


//收藏文章
function addFavoriteAsArticle(data: Favorite): Promise<R<string>> {
    return request({
        method: "put",
        url: 'blogFavorite/addFavoriteAsArticle',
        data,
    })
}

//删除收藏的文章
function deleteFavoriteAsArticle(favorite: Favorite): Promise<R<string>> {
    return request({
        method: "delete",
        url: 'blogFavorite/deleteFavoriteAsArticle',
        params: {
            userId: favorite.userId,
            articleId: favorite.articleId
        }
    })
}

//点赞文章
function doLikeAsArticle(data: any): Promise<R<string>> {
    return request({
        method: "put",
        url: 'blogLike/doLikeAsArticle',
        data
    })
}

//点赞文章
function deleteLikeAsArticle(userId: number, articleId: number): Promise<R<string>> {
    return request({
        method: "delete",
        url: 'blogLike/deleteLikeAsArticle',
        params: {
            userId,
            articleId
        }
    })
}

export {
    favoriteNameAsCount,
    favoriteNameList,
    addFavorite,
    favoriteAsArticle,
    editAsFavorite,
    deleteInFavoriteAsArticle,
    deleteAsFavorite,
    addFavoriteAsArticle,
    deleteFavoriteAsArticle,
    doLikeAsArticle,
    deleteLikeAsArticle
}