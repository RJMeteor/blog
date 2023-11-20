import request from '../../config/axios.config'
import {Login, Regist} from "../../define/login.request";
import {Article, Articles} from "../../define/creation.request";
import {R} from "../../define/r";
import {
    Articlepush, ChattingRecords, Comment,
    EventArticlepush,
    EventAttention,
    EventChattingRecords,
    EventComment,
    EventLike
} from "../../define/messagecenter";


//文章推送
function artclePush(userId: number): Promise<R<Array<EventArticlepush>>> {
    return request({
        method: "get",
        url: 'blogArticlePush/artclePush',
        params: {
            userId
        }
    })
}

/*
* 删除文章推送
* */
function deleteArticlePush(articlePushId: number): Promise<R<string>> {
    return request({
        method: "delete",
        url: 'blogArticlePush/deleteArticlePush',
        params: {
            articlePushId
        }
    })
}

function deleteArticlePushAllAsActive(userId: number): Promise<R<string>> {
    return request({
        method: "delete",
        url: 'blogArticlePush/deleteArticlePushAllAsActive',
        params: {
            userId
        }
    })
}

function updateArticlePushAllAsStatus(userId: number): Promise<R<string>> {
    return request({
        method: "post",
        url: 'blogArticlePush/updateArticlePushAllAsStatus',
        data: {
            userId
        },
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    })
}


//私信消息推送
function chatListAsActive(userId: number): Promise<R<Array<EventChattingRecords>>> {
    return request({
        method: "get",
        url: 'blogChattingRecords/chatListAsActive',
        params: {
            userId
        }
    })
}

//删除私信消息推送
function deleteChatListAsState(chatid: number): Promise<R<Array<EventChattingRecords>>> {
    return request({
        method: "delete",
        url: 'blogChattingRecords/deleteChatListAsState',
        params: {
            chatid
        }
    })
}


//评论消息推送
function commentListAsActive(userId: number): Promise<R<Array<EventComment>>> {
    return request({
        method: "get",
        url: 'blogComment/commentListAsActive',
        params: {
            userId
        }
    })
}

/*
* 删除评论消息推送
* */
function deleteCommentListAsActive(commentId: number): Promise<R<Array<EventComment>>> {
    return request({
        method: "delete",
        url: 'blogComment/deleteCommentListAsActive',
        params: {
            commentId
        }
    })
}

/*
* 把推送评论消息为已读
* */
function updateCommentListAsStatus(commentId: number): Promise<R<Array<EventComment>>> {
    return request({
        method: "post",
        url: 'blogComment/updateCommentListAsStatus',
        data: {
            commentId
        },
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    })
}

//回复评论
function sendCommentAsPushList(data: Comment): Promise<R<string>> {
    return request({
        method: "put",
        url: 'blogComment/sendCommentAsPushList',
        data
    })
}

function deleteCommentAllAsActive(userId: number): Promise<R<string>> {
    return request({
        method: "delete",
        url: 'blogComment/deleteCommentAllAsActive',
        params: {
            userId
        }
    })
}

function updateCommentAllAsStatus(userId: number): Promise<R<string>> {
    return request({
        method: "post",
        url: 'blogComment/updateCommentAllAsStatus',
        data: {
            userId
        },
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    })
}

//点赞消息推送
function likeListAsActive(userId: number): Promise<R<Array<EventLike>>> {
    return request({
        method: "get",
        url: 'blogLike/likeListAsActive',
        params: {
            userId
        }
    })
}

/*
* 删除点赞消息推送
* */
function deleteLikeListAsActive(likeId: number): Promise<R<Array<EventComment>>> {
    return request({
        method: "delete",
        url: 'blogLike/deleteLikeListAsActive',
        params: {
            likeId
        }
    })
}

/*
     * 剔除用户的推送列表
     * */
function deleteLikeAllAsActive(userId: number): Promise<R<string>> {
    return request({
        method: "delete",
        url: 'blogLike/deleteLikeAllAsActive',
        params: {
            userId
        }
    })
}

/*
     * 更改用户的推送列表为已读
     * */
function updateLikeAllAsStatus(userId: number): Promise<R<string>> {
    return request({
        method: "post",
        url: 'blogLike/updateLikeAllAsStatus',
        data: {
            userId
        },
        headers:{
            "Content-Type":"application/x-www-form-urlencoded"
        }
    })
}

//关注消息推送
function attentionListAsActvie(userId: number): Promise<R<Array<EventAttention>>> {
    return request({
        method: "get",
        url: 'blogAttention/attentionListAsActvie',
        params: {
            userId
        }
    })
}

/*
* 删除关注消息推送
* */
function deleteAsttentionListAsActive(asttentionId: number): Promise<R<Array<EventAttention>>> {
    return request({
        method: "delete",
        url: 'blogAttention/deleteAsttentionListAsActive',
        params: {
            asttentionId
        }
    })
}

function deleteAttentionAllAsActive(userId: number): Promise<R<Array<EventAttention>>> {
    return request({
        method: "delete",
        url: 'blogAttention/deleteAttentionAllAsActive',
        params: {
            userId
        }
    })
}

function updateAttentionAllAsStatus(userId: number): Promise<R<Array<EventAttention>>> {
    return request({
        method: "post",
        url: 'blogAttention/updateAttentionAllAsStatus',
        data: {
            userId
        },
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    })
}


/*
* 获取用户的聊天聊天记录
* */
function singleChatList(message: ChattingRecords): Promise<R<Array<ChattingRecords>>> {
    return request({
        method: "get",
        url: 'blogChattingRecords/singleChatList',
        params: {
            ...message
        }
    })
}

/*
* 给聊天用户的聊天状态更新到已读状态
* */
function updateChatListAsStatus(acceptorUserId: number, senderUserId: number): Promise<R<Object>> {
    return request({
        method: "post",
        url: 'blogChattingRecords/updateChatListAsStatus',
        data: {
            acceptorUserId,
            senderUserId
        },
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    })
}


/*
* 删除推送列表的消息
* */
function deleteChatListAsActive(chatid: number): Promise<R<Object>> {
    return request({
        method: "delete",
        url: 'blogChattingRecords/deleteChatListAsActive',
        params: {
            chatid
        }
    })
}


function deletePrivateletterAllAsActive(privateId: Array<number>): Promise<R<Object>> {
    return request({
        method: "delete",
        url: 'blogChattingRecords/deletePrivateletterAllAsActive',
        data: privateId,
    })
}

function updatePrivateletterAllAsStatus(privateId: Array<number>): Promise<R<Object>> {
    return request({
        method: "post",
        url: 'blogChattingRecords/updatePrivateletterAllAsStatus',
        data: privateId
    })
}

export {
    artclePush,
    deleteArticlePush,
    deleteArticlePushAllAsActive,
    updateArticlePushAllAsStatus,
    likeListAsActive,
    deleteLikeListAsActive,
    updateLikeAllAsStatus,
    deleteLikeAllAsActive,
    commentListAsActive,
    deleteCommentListAsActive,
    updateCommentListAsStatus,
    deleteCommentAllAsActive,
    updateCommentAllAsStatus,
    sendCommentAsPushList,
    attentionListAsActvie,
    deleteAsttentionListAsActive,
    deleteAttentionAllAsActive,
    updateAttentionAllAsStatus,
    chatListAsActive,
    updateChatListAsStatus,
    singleChatList,
    deleteChatListAsActive,
    deletePrivateletterAllAsActive,
    updatePrivateletterAllAsStatus
}