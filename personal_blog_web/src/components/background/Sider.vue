<script setup lang="ts">
    import {ref,onMounted} from "vue"
    import {NotificationsOutline, PersonCircle, SparklesOutline} from "@vicons/ionicons5"
    import {siderRender, siderMenu} from "./cover/sider"
    import {modalOptions} from "./cover/modal"
    import {useRouter} from "vue-router"
    import {MessageType} from "../../define/message";

    const show = ref<boolean>(false)
    const emit = defineEmits(["modalStateChange"])
    const activeMenu = ref<string>()
    const broadcastChannel = new BroadcastChannel(`message`)
    broadcastChannel.onmessage = function (e) {
        const message:MessageType = e.data;
        activeMenu.value = message.activeMenu
    }
    onMounted(function () {
        activeMenu.value = (JSON.parse(localStorage.getItem("message") as string ) as MessageType)?.activeMenu
    })

    function modalStateChange() {
        emit("modalStateChange", !show.value)
    }

    function handleSelect(key: string) {
        console.log(key)
    }
    const router = useRouter()
    function handleBack() {
        router.push({
            path:"/index"
        })
    }
</script>

<template>
    <n-layout-sider
            bordered
            :width="240"
            :native-scrollbar="false"
    >
        <div class="layout-sider">
            <n-layout-header>
                <n-page-header subtitle="返回" @back="handleBack">
                </n-page-header>
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
                 &>:nth-child(1){
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
</style>
