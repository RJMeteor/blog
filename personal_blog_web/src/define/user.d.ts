import {Time} from "./Time";
import {Art} from "./art";

type User = Time & {
    id?: number;

    /**
     * 用户名
     */
    userName?: string;

    /**
     * 密码
     */
    userPass?: string;

    /**
     * 电话号码
     */
    userPhone?: string;

    /**
     * 性别
     */
    userSex?: number;

    /**
     * 头像地址
     */
    userImage?: string;

    /**
     * 技术领域ID
     */
    userArtId?: number;

    /**
     * 个人简介
     */
    userIntro?: string;

}

type UserInfo = {
    /*
  * 关注
  * */
    attention: number;

    /**
     * 粉丝
     */
    fans: number;

    /**
     * 浏览
     */
    browse: number;

    /**
     * 文章
     */
    article: number;

    /**
     * 收藏
     */
    favorite: number;

    /**
     * 点赞
     */
    praise: number;

    /**
     * 用户
     */
    blogUser: User;

    /*
    * 技术领域集合
    * */
    blogArtList: Array<Art>
    /*
    * 用户关注Id列表
    * */
    attentionAsId:Array<number>
}
export {
    User,
    UserInfo
}