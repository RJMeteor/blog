<script setup lang="ts">
    import {ChatbubbleEllipsesSharp, Trash} from "@vicons/ionicons5"
    import {ref, onMounted, nextTick} from "vue";
    import {
        artclePush,
        deleteArticlePush, deleteArticlePushAllAsActive, deleteAttentionAllAsActive,
        updateArticlePushAllAsStatus,
        updateAttentionAllAsStatus
    } from "../../../../hook/request/messagecenter.request";
    import {EventArticlepush} from "../../../../define/messagecenter";
    import {User} from "../../../../define/user";
    import {useRouter} from "vue-router";
    //文章推送列表
    const articlePushList = ref<Array<EventArticlepush>>([])
    const router = useRouter()
    import Behavior from "../Behavior.vue"
    import {messageStatusView} from "../../cover/modal";
    import {EventType, Sse, Unions} from "../../../../hook/correspondence/sse";

    //删除文章推送
    function deleteArticlePushs(e: Event, index: number, articlePushId: number) {
        e.preventDefault()
        e.stopPropagation()
        deleteArticlePush(articlePushId).then(res => {
            if (res.code == 200) {
                articlePushList.value.splice(index, 1)
                messageStatusView()
            }
        })
    }

    onMounted(function () {
        let parse: User = JSON.parse(localStorage.getItem("user") as string);
        if (parse.id) {
            artclePush(parse.id).then(res => {
                articlePushList.value = res.data
            })
        }
    })

    function readerArticle(articleId: number) {
        router.push({
            path: `/reader/${articleId}`
        })
    }

    Sse.articlepushCallBack(function (event) {
        articlePushList.value.push(event)
    })

    const behaviorRef = ref<{
        readerState?: boolean,
        active?: boolean,
        readerStateCallback?: Function,
        activeCallback?: Function
    }>({})
    const user: User = JSON.parse(localStorage.getItem("user") as string)
    nextTick(function () {
        behaviorRef.value.readerStateCallback = () => {
            updateArticlePushAllAsStatus(user.id as number).then(res => {
                if (res.code == 200) {
                    behaviorRef.value.readerState = false
                    for (let ele of articlePushList.value) {
                        ele.readeStatus = 1
                        behaviorRef.value.readerState = false
                        messageStatusView()
                    }
                }
            })
        }
        behaviorRef.value.activeCallback = () => {
            deleteArticlePushAllAsActive(user.id as number).then(res => {
                if (res.code == 200) {
                    behaviorRef.value.active = false
                    behaviorRef.value.readerState = false
                    articlePushList.value.length = 0
                    behaviorRef.value.active = false
                    messageStatusView()
                }
            })
        }
    })
</script>

<template>
    <Behavior ref="behaviorRef"/>
    <n-list hoverable bordered v-for="(ele,index) in articlePushList">
        <n-list-item>
            <n-thing @click="readerArticle(ele.article?.blogArticleContentMapping?.article?.id)">
                <template #header-extra>
                    <n-icon size="20" @click="deleteArticlePushs($event,index,ele.articlepushId)" color="#d03050">
                        <Trash/>
                    </n-icon>
                </template>
                <template #header>
                    {{ele.article?.articleTitle}}
                </template>
                <template #description>
                    <div v-show="false">
                        {{behaviorRef.readerState ||= !ele.readeStatus}}
                        {{behaviorRef.active ||= 1}}
                    </div>
                    {{ele.createTime}} 来自关注
                    <b>{{ele.sendUser?.userName}}</b>
                    发的文章
                    <n-badge :show="!ele.readeStatus" dot/>
                </template>
            </n-thing>
        </n-list-item>
    </n-list>
</template>

<style lang="scss" scoped>
    .n-list {
        margin: 10px 0px;
        /*box-shadow: 0 0 10px 1px #f4f5f5;*/
        /*background-color: red;*/
    }
</style>
