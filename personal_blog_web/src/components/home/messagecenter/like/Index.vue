<script setup lang="ts">
    import {ChatbubbleEllipsesSharp, Trash} from "@vicons/ionicons5"
    import {onMounted, ref, nextTick} from "vue"
    import {User} from "../../../../define/user";
    import {
        commentListAsActive,
        deleteCommentListAsActive, deleteLikeAllAsActive, deleteLikeListAsActive,
        likeListAsActive, sendCommentAsPushList,
        updateCommentListAsStatus, updateLikeAllAsStatus
    } from "../../../../hook/request/messagecenter.request";
    import {Comment, EventComment, EventLike} from "../../../../define/messagecenter";
    import {useRouter} from "vue-router";
    import Behavior from "../Behavior.vue"
    import {messageStatusView} from "../../cover/modal";
    import {Sse} from "../../../../hook/correspondence/sse";
    //点赞列表
    const like = ref<Array<EventLike>>([])
    const router = useRouter()

    onMounted(function () {
        let parse: User = JSON.parse(localStorage.getItem("user") as string);
        if (parse.id) {
            likeListAsActive(parse.id).then(res => {
                like.value = res.data
            })
        }
    })

    Sse.likeCallBack(function (event) {
        like.value.push(event)
    })

    const user: User = JSON.parse(localStorage.getItem("user") as string)

    //删除点赞
    function deleteLike(index:number,likeId: number) {
        deleteLikeListAsActive(likeId).then(res=>{
            if (res.code == 200) {
                like.value.splice(index, 1)
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
            updateLikeAllAsStatus(user.id as number).then(res=>{
                if (res.code == 200) {
                    behaviorRef.value.readerState = false
                    for (let ele of like.value) {
                        ele.stated = 1
                        behaviorRef.value.readerState = false
                        messageStatusView()
                    }
                }
            })
        }
        behaviorRef.value.activeCallback = () => {
            deleteLikeAllAsActive(user.id as number).then(res=>{
                if (res.code == 200) {
                    behaviorRef.value.active = false
                    behaviorRef.value.readerState = false
                    like.value.length = 0
                    behaviorRef.value.active = false
                    messageStatusView()
                }
            })
        }
    })
</script>

<template>
    <div id="like">
        <Behavior ref="behaviorRef"/>
        <n-list bordered hoverable v-for="(ele,index) in like">
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
                    <template #header-extra>
                        <n-icon size="20" @click="deleteLike(index,ele.likeId)" color="#d03050">
                            <Trash/>
                        </n-icon>
                    </template>
                    <template #description>
                        <div v-show="false">
                            {{behaviorRef.readerState ||= !ele.readeStatus}}
                            {{behaviorRef.active ||= 1}}
                        </div>
                        {{ele.createTime}} 点赞了你的{{ele.article?.articleTitle}}文章
                        <n-badge :show="!ele.stated" dot/>
                    </template>
                </n-thing>
            </n-list-item>
        </n-list>
    </div>
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
