import {Time} from "./Time";
import {User} from "./user";
import {Article, Articles} from "./creation.request";

type FavoriteNameCount = {
    favoriteName: string;
    countAsName: number;
}

type Favorite = Time & {

    id: number;

    /**
     * 用户id
     */
    userId: number;

    /**
     * 收藏夹的名字
     */
    favoriteName: string;

    /**
     * 文章id
     */
    articleId: number;


    user: User;
    article: Articles;
}

export {
    FavoriteNameCount,
    Favorite
}