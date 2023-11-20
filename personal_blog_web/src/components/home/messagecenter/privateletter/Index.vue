<script setup lang="ts">
    import {ChatbubbleEllipsesSharp, Trash} from "@vicons/ionicons5"
    import {ref, onMounted, nextTick} from "vue"
    import {User} from "../../../../define/user";
    import {
        chatListAsActive,
        deleteChatListAsActive,
        deleteLikeAllAsActive,
        deletePrivateletterAllAsActive, updateChatListAsStatus,
        updateLikeAllAsStatus,
        updatePrivateletterAllAsStatus
    } from "../../../../hook/request/messagecenter.request";
    import {EventAttention, EventChattingRecords} from "../../../../define/messagecenter";
    import Modal from "../../../../define/components/Modal.vue"
    import {useRouter} from "vue-router";
    import Behavior from "../Behavior.vue"
    import {messageStatusView} from "../../cover/modal";
    import {Sse} from "../../../../hook/correspondence/sse";

    const modal = ref<typeof Modal>(null)
    const privateletterList = ref<Array<EventChattingRecords>>([])
    const router = useRouter()

    onMounted(function () {
        let parse: User = JSON.parse(localStorage.getItem("user") as string);
        if (parse.id) {
            chatListAsActive(parse.id).then(res => {
                privateletterList.value = res.data
            })
        }
    })
    Sse.privateletterCallBack(function (event) {
        privateletterList.value.push(event)
    })

    function viewPrivateLetter(index: number, userId: number) {
        modal.value.showModal = true
        modal.value.userId = userId
        updateChatListAsStatus(user.id as number, userId).then(res => {
            if (res.code == 200) {
                privateletterList.value[index].readeStatus = 1
                messageStatusView()
            }
        })
    }

    function deletePrivateLetter(index: number, chatid: number) {
        deleteChatListAsActive(chatid).then(res => {
            if (res.code == 200) {
                privateletterList.value.splice(index, 1)
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
    const user: User = JSON.parse(localStorage.getItem("user") as string)
    const privateId = new Set<number>()
    nextTick(function () {
        behaviorRef.value.readerStateCallback = () => {
            updatePrivateletterAllAsStatus(Array.from(privateId)).then(res => {
                if (res.code == 200) {
                    behaviorRef.value.readerState = false
                    for (let ele of privateletterList.value) {
                        ele.readeStatus = 1
                        messageStatusView()
                    }
                }
            })
        }
        behaviorRef.value.activeCallback = () => {
            console.log(Array.from(privateId))
            deletePrivateletterAllAsActive(Array.from(privateId)).then(res => {
                if (res.code == 200) {
                    behaviorRef.value.active = false
                    behaviorRef.value.readerState = false
                    privateletterList.value.length = 0
                    messageStatusView()
                }
            })
        }
    })

</script>

<template>
    <Modal ref="modal"/>
    <div id="privateletter">
        <Behavior ref="behaviorRef"/>
        <n-list hoverable bordered v-for="(ele,index) in privateletterList">
            <n-list-item>
                <template #prefix>
                    <n-avatar
                            round
                            :size="50"
                            @click="personalHomepage(
                                ele.sendUser?.id == user.id?
                                ele.acceptorUser?.id:ele.sendUser?.id)"
                            :src="ele.sendUser?.id == user.id ?
                             (ele.acceptorUser?.userImage?ele.sendUser?.userImage:'/icon.png')
                             :(ele.sendUser?.userImage?ele.sendUser?.userImage:'/icon.png')"
                    />
                </template>
                <n-thing :title="ele.sendUser?.id == user.id?
                                ele.acceptorUser?.userName:ele.sendUser?.userName" :title-extra="ele.createTime">
                    <template #description>
                        <div v-show="false">
                            {{behaviorRef.readerState ||= !ele.readeStatus}}
                            {{behaviorRef.active ||= 1}}
                        </div>
                        {{ele.comment}}
                    </template>
                    <n-space>
                        <n-badge :show="ele.sendUser?.id == user.id? false :!ele.readeStatus" dot>
                            <n-icon size="20" @click="viewPrivateLetter(index,
                                ele.sendUser?.id == user.id?
                                ele.acceptorUser?.id:ele.sendUser?.id)" color="#2080f0">
                                <ChatbubbleEllipsesSharp/>
                            </n-icon>
                        </n-badge>
                        <div v-show="false">{{privateId.add(ele.privateleId)}}</div>
                        <n-icon size="20" @click="deletePrivateLetter(index,ele.privateleId)" color="#d03050">
                            <Trash/>
                        </n-icon>
                    </n-space>
                </n-thing>
            </n-list-item>
        </n-list>
    </div>
</template>

<style lang="scss" scoped>
    #privateletter {
        height: 100vh;
    }

    ::v-deep(.n-layout-scroll-container) {
        padding: 0 !important;
    }

    .n-list {
        margin: 10px 0px;
    }

    .n-avatar {
        cursor: pointer;
        background-color: white !important;
    }
</style>
