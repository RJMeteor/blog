<script setup lang="ts">
    import {ChatbubbleEllipsesSharp, Trash} from "@vicons/ionicons5";
    import {nextTick, onMounted, ref} from "vue"
    import {User} from "../../../../define/user";
    import {
        deleteAsttentionListAsActive,
        attentionListAsActvie,
        updateLikeAllAsStatus, deleteLikeAllAsActive, updateAttentionAllAsStatus, deleteAttentionAllAsActive
    } from "../../../../hook/request/messagecenter.request";
    import {EventAttention} from "../../../../define/messagecenter";
    import {useRouter} from "vue-router";
    import Behavior from "../Behavior.vue"
    import {messageStatusView} from "../../cover/modal";
    import {Sse} from "../../../../hook/correspondence/sse";

    const attentionList = ref<Array<EventAttention>>([])
    const router = useRouter()

    //删除关注
    function deleteAttention(index: number, attentionId: number) {
        deleteAsttentionListAsActive(attentionId).then(res => {
            if (res.code == 200) {
                attentionList.value.splice(index, 1)
                messageStatusView()
            }
        })
    }

    onMounted(function () {
        let parse: User = JSON.parse(localStorage.getItem("user") as string);
        if (parse.id) {
            attentionListAsActvie(parse.id).then(res => {
                attentionList.value = res.data
            })
        }
    })

    function personalHomepage(userId: number) {
        router.push({
            path: `/homepage/${userId}`
        })
    }

    Sse.attentionCallBack(function (event) {
        attentionList.value.push(event)
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
            updateAttentionAllAsStatus(user.id as number).then(res => {
                if (res.code == 200) {
                    behaviorRef.value.readerState = false
                    for (let ele of attentionList.value) {
                        ele.stated = 1
                        behaviorRef.value.readerState = false
                        messageStatusView()
                    }
                }
            })
        }
        behaviorRef.value.activeCallback = () => {
            deleteAttentionAllAsActive(user.id as number).then(res => {
                if (res.code == 200) {
                    behaviorRef.value.active = false
                    behaviorRef.value.readerState = false
                    attentionList.value.length = 0
                    behaviorRef.value.active = false
                    messageStatusView()
                }
            })
        }
    })
</script>

<template>
    <Behavior ref="behaviorRef"/>
    <n-list bordered hoverable v-for="(ele,index) in attentionList">
        <n-list-item>
            <template #prefix>
                <n-avatar
                        round
                        :size="50"
                        @click="personalHomepage(ele.attentionUser?.id)"
                        :src="ele.attentionUser?.userImage?ele.attentionUser?.userImage:'/icon.png'"
                />
            </template>
            <n-thing :title="ele.attentionUser?.userName">
                <template #description>
                    <div v-show="false">
                        {{behaviorRef.readerState ||= !ele.stated}}
                        {{behaviorRef.active ||= 1}}
                    </div>
                    {{ele.createTime}} 关注了你
                    <n-badge :show="!ele.stated" dot/>
                </template>
                <template #header-extra>
                    <n-icon size="20" @click="deleteAttention(index,ele.attentionId)" color="#d03050">
                        <Trash/>
                    </n-icon>
                </template>
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
