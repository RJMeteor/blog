<script setup lang="ts">
    import {ref, onMounted, watch, nextTick} from "vue"
    import {NotificationsOutline, PersonCircle, SparklesOutline} from "@vicons/ionicons5"
    import {headerOptions, siderRender, siderMenu} from "./cover/sider"
    import {ger, modalOptions} from "./cover/modal"
    import {useRouter} from "vue-router"
    import {MessageType} from "../../define/message";
    import {User} from "../../define/user";
    import {messageAsStatus} from "../../hook/request/user.request";
    import Modal from "./messagecenter/Modal.vue";
    // import Modal from "./messagecenter/Modal.vue"
    const show = ref<boolean>(false)
    const emit = defineEmits(["modalStateChange"])
    const router = useRouter()
    const activeMenu = ref<string>()
    const broadcastChannel = new BroadcastChannel(`message`)
    broadcastChannel.onmessage = function (e) {
        const message: MessageType = e.data;
        activeMenu.value = message.activeMenu
    }

    function modalStateChange() {
        router.push({
            path: "/index/privateletter",
        })
        emit("modalStateChange", !show.value)
    }

    const user: User = JSON.parse(localStorage.getItem("user") as string)
    const showRed = ref<boolean>(false)

    onMounted(function () {
        activeMenu.value = (JSON.parse(localStorage.getItem("message") as string) as MessageType)?.activeMenu
        if (user?.id) {
            messageAsStatus(user?.id).then(res => {
                const re = res.data;
                showRed.value = re.comment || re.like || re.articlepush || re.attention || re.privateletter
            })
        }
    })

    function handleSelect(key: string) {
        if(Object.is(key,"loginout")){
            localStorage.clear()
            router.push({
                path:"/"
            })
        }
    }

    let broadcastChannel1 = new BroadcastChannel("showRed");
    broadcastChannel1.onmessage = function (e) {
        showRed.value = e.data
    }

    const modalEle = ref<typeof Modal>(null)
    nextTick(function () {
        modalEle.value.callback = () => {
            messageAsStatus(user?.id as number).then(res => {
                const re = res.data;
                showRed.value = re.comment || re.like || re.articlepush || re.attention || re.privateletter
            })
        }
    })

    function modalStateChange1(value: boolean) {
        modalEle.value.showModal = value;
    }

    const broadcastChannel3 = new BroadcastChannel(`message`)
    broadcastChannel3.onmessage = function (e) {
        const message: MessageType = e.data;
        try {
            modalEle.value.showModal = message.modalState ? message.modalState : false;
        } catch (e) {
        }
    }
</script>

<template>
    <Modal ref="modalEle" :show="modalState" @modalStateChange="modalStateChange1"/>
    <n-layout-sider
            bordered
            :width="240"
            :native-scrollbar="false"
    >
        <div class="layout-sider">
            <n-layout-header>
                <n-image
                        width="40"
                        src="/icon.png"
                        preview-disabled
                />
                <div class="header_box">
                    <n-badge :show="showRed" dot>
                        <n-icon size="17" @click="modalStateChange" color="#0e7a0d">
                            <NotificationsOutline/>
                        </n-icon>
                    </n-badge>
                    <n-dropdown
                            :options="headerOptions"
                            placement="bottom-start"
                            trigger="click"
                            @select="handleSelect"
                            :render-label="siderRender"
                    >
                        <n-icon size="25" color="#666666">
                            <PersonCircle/>
                        </n-icon>
                    </n-dropdown>
                </div>
            </n-layout-header>
            <n-layout-content>
                <n-menu
                        v-model:value="activeMenu"
                        :collapsed-width="0"
                        :collapsed-icon-size="22"
                        :options="siderMenu"
                />
            </n-layout-content>
            <n-layout-footer>
                <n-icon size="25" color="red">
                    <!--                    <SparklesOutline/>-->
                </n-icon>
            </n-layout-footer>
        </div>
    </n-layout-sider>

</template>

<style lang="scss" scoped>
    .layout-sider {
        height: 100vh;

        .n-layout-content {
            background-color: #f4f5f5;
            --n-item-color-hover: white;
            padding: 0;
            width: 100%;
            height: calc(100vh - 90px);
        }

        .n-layout-header {
            background-color: #f4f5f5;
            height: 45px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding-right: 18px;
            padding-left: 18px;

            .header_box {
                display: flex;
                justify-content: center;
                align-items: center;

                & > :nth-child(1) {
                    margin-right: 10px;
                    display: flex;
                    justify-content: flex-end;
                }
            }
        }

        .n-layout-footer {
            background-color: #f4f5f5;
            height: 45px;
        }
    }

    ::v-deep(.n-menu-item) {
        --n-item-color-hover: white;
    }
</style>
