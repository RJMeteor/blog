<script setup lang="ts">
    import {ChatbubbleEllipsesSharp, Trash} from "@vicons/ionicons5"
    import {nextTick, onMounted, ref} from "vue"
    import {User} from "../../../../define/user";
    import {
        commentListAsActive, deleteCommentAllAsActive,
        deleteCommentListAsActive, deleteLikeListAsActive, deletePrivateletterAllAsActive,
        likeListAsActive, sendCommentAsPushList, updateCommentAllAsStatus,
        updateCommentListAsStatus, updatePrivateletterAllAsStatus
    } from "../../../../hook/request/messagecenter.request";
    import {Comment, EventComment, EventLike} from "../../../../define/messagecenter";
    import {useRouter} from "vue-router";
    import Behavior from "../Behavior.vue"
    import {messageStatusView} from "../../cover/modal";
    import {Sse} from "../../../../hook/correspondence/sse";
    import {sendComment} from "../../../../hook/request/creation.request";
    //评论列表
    const comment = ref<Array<EventComment>>([])
    const router = useRouter()

    onMounted(function () {
        let parse: User = JSON.parse(localStorage.getItem("user") as string);
        if (parse.id) {
            commentListAsActive(parse.id).then(res => {
                comment.value = res.data
            })
        }
    })
    Sse.commentCallBack(function (event) {
        comment.value.push(event)
    })

    const stats = ref<boolean>(false)
    //回复的内容
    const pushComment = ref<string>()
    const commentInfo = ref<{
        userId?: number,
        articleId?: number,
        commentKeyId?: number
    }>({})
    //打开回复评论弹窗
    /*
    * userId:被回复评论的用户id
    * articleId:文章id
    * */
    function viewComment(index: number, commentKeyId: number, commentId: number, userId: number, articleId: number) {
        commentInfo.value = {}
        pushComment.value = ""
        commentInfo.value!.userId = userId
        commentInfo.value!.articleId = articleId
        commentInfo.value!.commentKeyId = commentId
        stats.value = true
        updateCommentListAsStatus(commentId).then(res => {
            if (res.code == 200) {
                comment.value[index].stated = 1
                messageStatusView()
            }
        })
    }

    const user: User = JSON.parse(localStorage.getItem("user") as string)

    //回复评论
    function sendMessage() {
        const comments: Comment = {
            personId: commentInfo.value.userId as number,
            userId: user.id as number,
            //@ts-ignore
            articleId: commentInfo.value.articleId,
            commentContentMapping: {
                //@ts-ignore
                commentContent: pushComment.value
            },
            commentKeyId: commentInfo.value.commentKeyId as number
        }
        if (pushComment.value && pushComment.value.trim().length > 0) {
            // sendCommentAsPushList(comments).then(res => {
            //     if (res.code == 200) {
            //         stats.value = false
            //     }
            // })
            sendComment(comments).then(res => {
                if (res.code == 200) {
                    stats.value = false
                    window.$message.success("评论成功")
                }
            })
        } else {
            window.$message.error("回复评论内容不能为空")
        }
    }

    //删除评论
    function deleteComment(index: number, commentId: number) {
        deleteCommentListAsActive(commentId).then(res => {
            if (res.code == 200) {
                comment.value.splice(index, 1)
                messageStatusView()
            }
        })
    }


    function personalHomepage(userId: number) {
        router.push({
            path: `/homepage/${userId}`
        })
    }

    const behaviorRef = ref<{
        readerState?: boolean,
        active?: boolean,
        readerStateCallback?: Function,
        activeCallback?: Function
    }>({})
    nextTick(function () {
        behaviorRef.value.readerStateCallback = () => {
            updateCommentAllAsStatus(user.id as number).then(res => {
                if (res.code == 200) {
                    behaviorRef.value.readerState = false
                    for (let ele of comment.value) {
                        ele.stated = 1
                        behaviorRef.value.readerState = false
                        messageStatusView()
                    }
                }
            })
        }
        behaviorRef.value.activeCallback = () => {
            deleteCommentAllAsActive(user.id as number).then(res => {
                if (res.code == 200) {
                    behaviorRef.value.active = false
                    behaviorRef.value.readerState = false
                    comment.value.length = 0
                    behaviorRef.value.active = false
                    messageStatusView()
                }
            })
        }
    })

</script>

<template>
    <n-modal v-model:show="stats">
        <n-card
                style="width: 260px"
                title="回复评论"
                :bordered="false"
        >
            <template #header-extra>
                <n-icon size="20" color="red" @click="stats=!stats">
                    <Close/>
                </n-icon>
            </template>
            <n-input v-model:value="pushComment" placeholder="回复评论内容..."/>
            <template #footer>
                <n-button type="success" @click="sendMessage">
                    回复
                </n-button>
            </template>
        </n-card>
    </n-modal>
    <Behavior ref="behaviorRef"/>
    <n-list bordered hoverable v-for="(ele,index) in comment">
        <n-list-item>
            <template #prefix>
                <n-avatar
                        round
                        :size="50"
                        @click="personalHomepage(ele.sendUser?.id)"
                        :src="ele.sendUser?.userImage?ele.sendUser?.userImage:'/icon.png'"
                />
            </template>
            <n-thing :title="ele.sendUser?.userName" :title-extra="ele.createTime">
                <template #description>
                    <div v-show="false">
                        {{behaviorRef.readerState ||= !ele.stated}}
                        {{behaviorRef.active ||= 1}}
                    </div>
                  {{ele.commentKeyId ? '回复了你的评论' :'评论了你的'+ele.article?.articleTitle+'文章' }}:{{ele.comment}}
                </template>
                <n-space>
                    <n-badge :show="!ele.stated" dot>
                        <n-icon size="20"
                                @click="viewComment(index,ele.commentKeyId,ele.commentId,ele.sendUser?.id,ele.article?.id)"
                                color="#2080f0">
                            <ChatbubbleEllipsesSharp/>
                        </n-icon>
                    </n-badge>
                    <n-icon size="20" @click="deleteComment(index,ele.commentId)" color="#d03050">
                        <Trash/>
                    </n-icon>
                </n-space>

            </n-thing>
        </n-list-item>
    </n-list>
</template>

<style lang="scss" scoped>
    .n-list {
        margin: 10px 0px;
    }

    .n-avatar {
        cursor: pointer;
        background-color: white !important;
    }
</style>
