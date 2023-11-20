import {Comment} from "../../define/messagecenter";
import {Component, h, ref, Ref} from "vue";
// @ts-ignore
import {NAvatar, NCollapse, NCollapseItem, NThing} from "naive-ui";
import {useRouter} from "vue-router";
import router from "../../config/router.config";

//回复用户的id
const personUserId = ref<number>(0)
//回复评论的id
const sendCommentKeyId = ref<number>(0)
/*
* 回复的提示文字
* */
const commentPlaceholder = ref<string>("输入评论内容....")

/**
 * 当前回复的评论对象
 */
const concurrentComment = ref<Comment>()

//评论内容
const commentContent = ref<string>()

/*
    * 处理多级评论元素
    * */
function generateComments(comments: Array<Comment>): Component[] {
    const childrenComponent: Array<Component> = []
    if (comments) {
        for (let comment of comments) {
            childrenComponent.push(returnChilder(comment, comment.isChildren))
        }
    }
    return childrenComponent
}


/*
* 返回渲染子元素
* */
function returnChilder(comment: Comment,
                       isChildren: boolean): Component {
    return h(
        NCollapseItem,
        {
            name: comment.id,
            disabled: !isChildren,
        },
        {
            header: () => h(
                NThing,
                {
                    onClick: function (e: Event) {
                        e.stopPropagation()
                        e.preventDefault()
                        const ele: Comment = comment
                        concurrentComment.value = comment
                        commentPlaceholder.value = "回复:" + comment.user?.userName as string
                        personUserId.value = comment.user.id as number;
                        sendCommentKeyId.value = comment.id
                        commentContent.value = ""
                    }
                },
                {
                    "header-extra": () => comment.createTime,
                    description: comment.commentContentMapping?.commentContent,
                    avatar: () => h(
                        NAvatar,
                        {
                            round: true,
                            size: 40,
                            src: `${comment.user?.userImage ? comment.user?.userImage : '/icon.png'}`,
                            onClick: function (e: Event) {
                                e.preventDefault()
                                e.stopPropagation()
                                router.push({
                                    path: `/homepage/${comment.user?.id}`
                                })
                            }
                        }
                    ),
                    header: comment.user?.userName
                },
            ),
            default: () => h(NCollapse,
                null,
                () => generateComments(comment.children))
        }
    )
}

export {
    generateComments,
    personUserId,
    sendCommentKeyId,
    commentPlaceholder,
    router,
    concurrentComment,
    commentContent
}
