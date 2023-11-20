import {h, ref, Ref} from "vue"
import {User} from "../../define/user";
import {Article} from "../../define/creation.request";
import {
    EventArticlepush,
    EventAttention,
    EventChattingRecords,
    EventComment,
    EventLike
} from "../../define/messagecenter";
import {NAvatar, NIcon, NTag} from "naive-ui";
import {MessageType} from "../../define/message";

const alterMessage = {
    articlepush: "文章消息推送",
    attention: "关注消息推送",
    comment: "评论消息推送",
    like: "点赞消息推送",
    privateletter: "私信消息推送"
}

enum EventType {
    Articlepush = "articlepush",
    Attention = "attention",
    Comment = "comment",
    Like = "like",
    Privateletter = "privateletter"
}

type MessageCenterTime = {
    createTime: string,
}

type Privateletter = MessageCenterTime & {
    privateleId: number,
    senderUser: User,
    content: string,
}


type Comment = MessageCenterTime & {
    commentId: number,
    sendUser: User,
    article: Article,
    comment: string
}


type Articlepush = MessageCenterTime & {
    articlepushId: number,
    acceptUser: User,
    article: Article,
    comment: string
}

type Attention = MessageCenterTime & {
    attentionId: number,
    attentionUser: User,
}

type Like = MessageCenterTime & {
    likeId: number,
    sendUser: User,
    article: Article,
}
type AlterMessage = {
    acceptUser: User,
    content: string,
    createTime: string,
    type: EventType
}

type Unions = EventArticlepush | EventComment | EventChattingRecords | EventLike | EventAttention

const excludes: Array<string> = ["articlepush", "attention", "privateletter", "commentlike"]

class Sse {
    static sseEmiter: EventSource
    static #broadcastChannel: BroadcastChannel
    static callBackMap = new Map<EventType, (event: Unions) => void>();


    constructor() {
        const user: User = JSON.parse(localStorage.getItem("user") as string)
        if (!Sse.sseEmiter) {
            Sse.sseEmiter = new EventSource(`${import.meta.env.BLOG_REQUEST_URL}sse/connection/${user.id}`)
            Sse.sseEmiter.addEventListener(EventType.Articlepush, this.#articlepush)
            Sse.sseEmiter.addEventListener(EventType.Attention, this.#attention)
            Sse.sseEmiter.addEventListener(EventType.Comment, this.#comment)
            Sse.sseEmiter.addEventListener(EventType.Like, this.#like)
            Sse.sseEmiter.addEventListener(EventType.Privateletter, this.#privateletter)
            Sse.#broadcastChannel = new BroadcastChannel("showRed");
        }
    }

    /*
    * 私信和推送文章需要未读红点
    * */
    static #messageAlter(message: AlterMessage) {
        const activeMenu = JSON.parse(localStorage.getItem("activeMenu") as string)
        const link = JSON.parse(localStorage.getItem("link") as string)
        const messages:MessageType = JSON.parse(localStorage.getItem("message") as string)
        if ((!excludes.includes(activeMenu)) && !messages.modalState) {
            //    消息推送提示
            window.$notification.create({
                title: message.acceptUser.userName,
                content: message.content.length > 10 ? message.content.substring(0, 10) + "..." : message.content,
                description: () =>
                    h(NTag, {
                            bordered: false,
                            type: "info",
                            size: "small"
                        },
                        {
                            default: () => `${alterMessage[message.type]}`
                        }),
                duration: 3000,
                meta: message.createTime,
                avatar: () =>
                    h(NAvatar, {
                        size: 'small',
                        round: true,
                        src: message.acceptUser?.userImage ? message.acceptUser?.userImage : '/icon.png'
                    }),
            })
        } else if (excludes.includes(activeMenu) && (link.indexOf(activeMenu)) < 0) {
            //    消息提示红点

        } else if (excludes.includes(activeMenu) && (link.indexOf(activeMenu)) >= 0) {
            //    添加消息

        }
        Sse.#broadcastChannel.postMessage(true)
    }

    /*
    * 文章推送消息
    * */
    #articlepush(event: MessageEvent) {
        const message: EventArticlepush = JSON.parse(event.data)
        Sse.callBackMap.get(EventType.Articlepush) && Sse.callBackMap.get(EventType.Articlepush)!(message)
        const alterMs: AlterMessage = {
            acceptUser: message.sendUser,
            content: message.comment,
            createTime: message.createTime,
            type: EventType.Articlepush
        }
        Sse.#messageAlter(alterMs)
    }

    static articlepushCallBack(fun: (event: EventArticlepush) => void): void {
        Sse.callBackMap.set(EventType.Articlepush,fun as (event: Unions) => void);
    }

    static attentionCallBack(fun: (event: EventAttention) => void) {
        Sse.callBackMap.set(EventType.Attention, fun as (event: Unions) => void);
    }

    static commentCallBack(fun: (event: EventComment) => void) {
        Sse.callBackMap.set(EventType.Comment, fun as (event: Unions) => void);

    }

    static likeCallBack(fun: (event: EventLike) => void) {
        Sse.callBackMap.set(EventType.Articlepush, fun as (event: Unions) => void);

    }

    static privateletterCallBack(fun: (event: EventChattingRecords) => void) {
        Sse.callBackMap.set(EventType.Articlepush, fun as (event: Unions) => void);
    }

    /*
    * 关注消息
    * */
    #attention(event: MessageEvent) {
        const message: EventAttention = JSON.parse(event.data)
        Sse.callBackMap.get(EventType.Attention) && Sse.callBackMap.get(EventType.Attention)!(message)
        const alterMs: AlterMessage = {
            acceptUser: message.attentionUser,
            content: "关注了你",
            createTime: message.createTime,
            type: EventType.Attention
        }
        Sse.#messageAlter(alterMs)
    }

    /*
    * 评论消息
    * */
    #comment(event: MessageEvent) {
        const message: EventComment = JSON.parse(event.data)
        Sse.callBackMap.get(EventType.Comment) && Sse.callBackMap.get(EventType.Comment)!(message)
        const alterMs: AlterMessage = {
            acceptUser: message.sendUser,
            content: message.comment,
            createTime: message.createTime,
            type: EventType.Comment
        }
        Sse.#messageAlter(alterMs)
        console.log(message)

    }

    /*
    * 点赞消息
    * */
    #like(event: MessageEvent) {
        const message: EventLike = JSON.parse(event.data)
        Sse.callBackMap.get(EventType.Like) && Sse.callBackMap.get(EventType.Like)!(message)
        const alterMs: AlterMessage = {
            acceptUser: message.sendUser,
            content: `点赞了你的${message.article.articleTitle}文章`,
            createTime: message.createTime,
            type: EventType.Like
        }
        Sse.#messageAlter(alterMs)
        console.log(message);
    }

    /*
    * 私信消息
    * */
    #privateletter(event: MessageEvent) {
        const message: EventChattingRecords = JSON.parse(event.data)
        Sse.callBackMap.get(EventType.Privateletter) && Sse.callBackMap.get(EventType.Privateletter)!(message)
        const alterMs: AlterMessage = {
            acceptUser: message.sendUser,
            content: message.comment,
            createTime: message.createTime,
            type: EventType.Privateletter
        }
        Sse.#messageAlter(alterMs)
        console.log(message);
    }
}

export {Sse, EventType}
export type {
    Unions,
    Privateletter, Comment, Like, Article, Articlepush, Attention
}
