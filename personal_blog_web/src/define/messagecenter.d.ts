import {Time} from "./Time";
import {Art} from "./art";
import {Article, Articles} from "./creation.request";
import {User} from "./user";

type Articlepush = Time & {
    id: number;
    /**
     * 文章id
     */
    articleId: number;
    /**
     * 接收用户id
     */
    acceptUserId: number;
    /**
     * 发送者用户id
     */
    sendUserId: number;
    /**
     * 0:未读，1:已读
     */
    readeStatus: number;

    /**
     *
     * 0:在推送列表里，1：不在
     */
     active:number;


    article: Articles;

    acceptUser: User;

    sendUser: User;
}

type EventArticlepush = Time & {
    articlepushId: number;
    acceptUser: User;
    sendUser: User;
    article: Articles;
    comment: string;
    createTime: string;
    readeStatus: number;
}

type Comment = Time & {

    id: number;
    /**
     * 用户id
     */
    userId: number;
    /**
     * 评论的id
     * */
    commentKeyId : number;
    /*
    * 父级评论Id
    * */
    personId:number;
    /**
     *
     * 0:在推送列表里，1：不在
     */
    active:number;

    /**
     *
     * 是否已读
     */
    stated:number;

    /**
     * 文章id
     */
    articleId: number;
    /**
     * 评论id
     */
    commentContentMappingId: number;
    user: User;
    personUser: User;

    article: Articles;

    commentContentMapping: CommentContentMapping;

    children:Array<Comment>
    isChildren:boolean
}
type EventComment = Time & {
    commentId: number;
    sendUser: User;
    article: Articles;
    comment: string;
    createTime: string;
    /**
     *
     * 是否已读
     */
    stated:number;
}

type CommentContentMapping = {
    id: number;
    commentContent: string;
}


type ChattingRecords = Time & {

    id?: number;

    /**
     * 发送方用户id
     */
    senderUserId: number|undefined;

    /**
     * 接收方用户id
     */
    acceptorUserId: number|undefined;

    /**
     * 聊天内容id
     */
    chattingRecordsContentMappingId?: number;

    /**
     * 0:未读，1:已读
     */
    readeStatus?: number;


    senderUser?: User;
    acceptorUser?: User;

    chattingRecordsContentMapping?: ChattingRecordsContentMapping;
}

type EventChattingRecords = Time & {
    privateleId: number;
    sendUser: User;
    acceptorUser:User;
    comment: string;
    createTime: string;
    /**
     * 0:未读，1:已读
     */
    readeStatus?: number;
}

type ChattingRecordsContentMapping = {
    id: number;

    /**
     * 聊天内容
     */
    chattingRecordsContent: string;
}


type EventLike = {
    likeId: number;
    sendUser: User;
    article: Articles;
    createTime: string;
    /**
     *
     * 0:在推送列表里，1：不在
     */
    stated:number;
}

type EventAttention = {
    attentionId: number;
    attentionUser: User;
    createTime: string;
    /*
    * 0：未读，1：已读
    * */
    stated:number;
}
export {
    Articlepush,
    Comment,
    CommentContentMapping,
    ChattingRecords,
    ChattingRecordsContentMapping,
    EventArticlepush,
    EventAttention,
    EventChattingRecords,
    EventComment,
    EventLike
}