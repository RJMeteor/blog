<script setup lang="ts">
    import {ref, watch, onMounted} from "vue"
    import {useRouter} from "vue-router"
    import {ger, messageStatusView, modalOptions} from "../cover/modal"
    import {MessageType} from "../../../define/message";
    import {MenuInst} from "naive-ui";
    import {User} from "../../../define/user";
    import {messageAsStatus} from "../../../hook/request/user.request";

    const props = defineProps(['show'])
    const emit = defineEmits(["modalStateChange"])
    const showModal = ref<boolean>(props.show)
    const router = useRouter()
    const selectedKey = ref<string>()
    const modalWidth = ref<number>(document.body.clientWidth)
    const callback = ref<Function>(()=>{})
    defineExpose({
        showModal,
        callback
    })

    const broadcastChannel = new BroadcastChannel(`message`)
    broadcastChannel.onmessage = function (e) {
        const message: MessageType = e.data;
        selectedKey.value = message.activeMenu
    }

    function re() {
        const link: string = JSON.parse(localStorage.getItem("link") as string);
        showModal.value = false
        router.push({
            path: link || "/index"
        })
    }

    onMounted(function () {
        const message: MessageType = JSON.parse(localStorage.getItem("message") as string)
        showModal.value = message.modalState as boolean
        selectedKey.value = message.activeMenu
        modalWidth.value = document.body.clientWidth
    })
    watch(showModal, value => {
        const link: string = JSON.parse(localStorage.getItem("link") as string);
        messageStatusView()
        if (value) {
            window.addEventListener("resize", re)
        } else {
            callback.value()
            window.removeEventListener("resize", re)
            showModal.value = false
            router.push({
                path: link || "/index"
            })
        }
    })
    function modalStateChange() {
        router.push({
            path: "/index/privateletter",
        })
        emit("modalStateChange", !showModal.value)
    }
</script>

<template>

    <n-drawer
            v-model:show="showModal"
            v-model:defaultWidth="modalWidth"
            placement="right"
            resizable
    >
        <n-drawer-content title="消息中心" closable>
            <n-layout has-sider>
                <n-layout-sider>
                    <n-menu
                            v-model:value="selectedKey"
                            :collapsed-width="0"
                            :collapsed-icon-size="22"
                            :options="modalOptions"
                    />
                </n-layout-sider>
                <n-layout>
                    <n-layout-content content-style="padding: 24px;">
                        <RouterView name="message" v-slot="{ Component }">
                            <Transition
                                    :duration="{enter:600,leave:500}"
                                    enter-active-class="animate__animated animate__fadeIn"
                                    leave-active-class="animate__animated animate__fadeOut">
                                <component :is="Component"/>
                            </Transition>
                        </RouterView>
                    </n-layout-content>
                </n-layout>
            </n-layout>
        </n-drawer-content>
    </n-drawer>

</template>

<style lang="scss" scoped>
    .n-card.n-modal {
        --n-padding-left: 0;

        .n-card__content {
            padding: 0 !important;
        }
    }

    ::v-deep(.n-menu-item) {
        --n-item-color-hover: white;
    }

    ::v-deep(.n-layout-sider) {
        height: calc(100vh - 51px);
        background-color: #f4f5f5;
    }

    ::v-deep(.n-layout-content) {
        background-color: white;
        height: calc(100vh - 51px);
    }

    ::v-deep(.n-drawer-body-content-wrapper) {
        padding: 0 !important;
        background-color: #f3f3f3 !important;
    }
</style>
