<script setup lang="ts">
    import {ref, onMounted, watch, nextTick} from "vue"
    import {ChattingRecords} from "../messagecenter";
    import {User} from "../user";
    import {singleChatList, updateChatListAsStatus} from "../../hook/request/messagecenter.request";
    import {WebsocketUtil} from "../../hook/correspondence/websocker";

    //聊天对话框的大小
    const bodyStyle = ref<Object>({
        width: 'calc(100vw / 1.5)',
        height: 'calc(100vh - 50px)'
    })
    const bodyHeight = ref<number>(document.body.offsetHeight / 2)
    //聊天对话框的显示状态
    const showModal = ref<boolean>(false)
    //用户id
    const userId = ref<number>()
    //消息列表
    const messageList = ref<Array<ChattingRecords>>([])
    //接受者id
    const acceptorUserId = ref<number>()
    //要发送的消息
    const sendMessageComment = ref<string>()
    let websocketUtil = new WebsocketUtil();

    defineExpose({
        showModal,
        userId
    })

    watch(showModal, value => {
        // localStorage.setItem("modalStatus", JSON.stringify(value))
        if (value) {
            nextTick(function () {
                let lastChild = document.getElementById("comment")?.lastElementChild
                lastChild?.scrollIntoView({
                    behavior: "auto",
                    block: "end",
                    inline: 'nearest'
                })
            })
            const user: User = JSON.parse(localStorage.getItem("user") as string)
            acceptorUserId.value = user.id
            const message: ChattingRecords = {
                acceptorUserId: user.id,
                senderUserId: userId.value
            }
            updateChatListAsStatus(message.acceptorUserId as number, message.senderUserId as number)
            singleChatList(message).then(res => {
                messageList.value = res.data
            })
        }
    })

    type messageType = {
        acceptorUserId?: number,
        senderUserId?: number,
        content?: string,
    }

    function sendMessage() {
        const message: messageType = {
            acceptorUserId: userId.value,
            senderUserId: acceptorUserId.value,
        }
        if (sendMessageComment.value?.trim().length) {
            message.content = sendMessageComment.value
            sendMessageComment.value = ""
            send(message)
        } else {
            window.$message.error("聊天内容不能为空")
        }
    }


    function send(message: messageType) {
        WebsocketUtil.webSocket.send(JSON.stringify(message))

    }

    const timer = setInterval(function () {
        if (WebsocketUtil.webSocket) {
            WebsocketUtil.webSocket.onmessage = function (e) {
                const messages: ChattingRecords = JSON.parse(e.data);
                messageList.value.push(messages)
                nextTick(function () {
                    let lastChild = document.getElementById("comment")?.lastElementChild
                    lastChild?.scrollIntoView({
                        behavior: "smooth",
                        block: "end",
                        inline: 'nearest'
                    })
                })
            }
            clearInterval(timer)
        }
    }, 1000)


</script>

<template>
    <div id="modal">
        <n-modal
                v-model:show="showModal"
                class="custom-card"
                preset="card"
                :style="bodyStyle"
                :title="messageList[0]?.senderUser?.id == acceptorUserId?messageList[0]?.acceptorUser?.userName: messageList[0]?.senderUser?.userName "
                size="huge"
                :bordered="false"
        >
            <div id="comment" :style="{height:bodyHeight+20+'px'}">
                <div v-for="(ele,index) in messageList" :key="index">
                    <p style="text-align: center;color: #dddddd;"
                       v-if="(((new Date(ele.createTime).getTime()
                       -
                       new Date(messageList[index-1]?.createTime).getTime())>180*1000) || index == 0)
                        && ele.acceptorUser?.id == acceptorUserId || ele.senderUserId?.id == acceptorUserId ">
                        {{ele.createTime}}
                    </p>
                    <div class="message right">
                        <div v-if="ele.acceptorUser?.id == userId || ele.senderUserId?.id == userId "
                             class="message right">
                            {{ele.chattingRecordsContentMapping?.chattingRecordsContent}}
                        </div>
                    </div>
                    <div class="message left">
                        <div v-if="ele.acceptorUser?.id == acceptorUserId || ele.senderUserId?.id == acceptorUserId"
                             class="message left">
                            {{ele.chattingRecordsContentMapping?.chattingRecordsContent}}
                        </div>
                    </div>
                </div>
            </div>
            <template #footer>
                <n-input
                        v-model:value="sendMessageComment"
                        placeholder="输入聊天内容...."
                        type="textarea"
                        size="small"
                        clearable
                        :autosize="{
                        minRows: 5,
                        maxRows: 5
                      }"
                />
                <n-button @click="sendMessage" type="primary">
                    发送
                </n-button>
            </template>
        </n-modal>
    </div>
</template>

<style lang="scss" scoped>
    .n-button {
        margin-top: 20px;
        float: right;
    }

    #comment {
        /*height: 400px;*/
        overflow-y: scroll;

        &::-webkit-scrollbar {
            width: 0px;
            height: 8px;
            background-color: #61B6EB;
        }

        &::-webkit-scrollbar-track {
            background: #eee;
        }

        &::-webkit-scrollbar-thumb {
            background: #61B6EB;
            border-radius: 100px;
        }

        &::-webkit-scrollbar-thumb:hover {
            background: #61B6EB;
        }

        &::-webkit-scrollbar-corner {
            background: #61B6EB;
        }

        > div {
            clear: both;
        }

        .message {
            display: flex;

            &.left {
                /*position: relative;*/
                background-color: var(--left-color);
                justify-content: flex-start;
                /*&:before {*/
                /*position: absolute;*/
                /*left: -10px;*/
                /*top: 36%;*/
                /*content: "";*/
                /*border-top: var(--size) solid transparent;*/
                /*border-right: var(--size) solid var(--left-color);*/
                /*border-bottom: var(--size) solid transparent;*/
                /*border-left: var(--size) solid transparent;*/
                /*}*/
            }

            &.right {
                position: relative;
                background-color: var(--right-color);
                justify-content: flex-end;
                color: white;

                /*&:after {*/
                /*    position: absolute;*/
                /*    left: 53px;*/
                /*    top: 36%;*/
                /*    content: "";*/
                /*    border-top: var(--size) solid transparent;*/
                /*    border-left: var(--size) solid var(--right-color);*/
                /*    border-bottom: var(--size) solid transparent;*/
                /*    border-right: var(--size) solid transparent;*/
                /*}*/
            }

            & > div {
                width: fit-content;
                padding: 10px;
                margin-top: 20px;
                border-radius: 10px;
                --left-color: #f5f5f5;
                --right-color: #36ad6a;
                --size: 5px;
            }
        }
    }
</style>
