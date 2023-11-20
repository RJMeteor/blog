<script setup lang="ts">
    import Message from './components/Message.vue'
    import Notification from './components/Notification.vue'
    import {darkTheme} from 'naive-ui'
    import {Sse} from "./hook/correspondence/sse";
    import {useMessage} from "naive-ui"
    import {User} from "./define/user";
    const user: User = JSON.parse(localStorage.getItem("user") as string)
    if(!user?.id){
        localStorage.setItem("user", JSON.stringify({id:undefined} as User))
    }else {
        new Sse();
    }
</script>

<template>
    <n-message-provider>
        <Message/>
    </n-message-provider>
    <n-notification-provider placement="bottom-right" :max="3">
        <Notification/>
    </n-notification-provider>
    <n-config-provider :theme-overrides="{ common: { fontWeightStrong: '600' } }">
        <RouterView v-slot="{ Component }">
            <Transition
                    :duration="{enter:600,leave:500}"
                    enter-active-class="animate__animated animate__fadeIn"
                    leave-active-class="animate__animated animate__fadeOut">
                <component :is="Component"/>
            </Transition>
        </RouterView>
    </n-config-provider>
</template>

<style lang="scss">
    body, html {
        font-size: 16px;
        width: 100vw;
        height: 100vh;
        overflow: hidden;
    }

    #app {
        margin: 0;
        padding: 0;
        text-align: left;
    }

    .md-editor-dark {
        --md-color: #999;
        --md-hover-color: #bbb;
        --md-bk-color: #000;
        --md-bk-color-outstand: #111;
        --md-bk-hover-color: #1b1a1a;
        --md-border-color: #2d2d2d;
        --md-border-hover-color: #636262;
        --md-border-active-color: #777;
        --md-modal-mask: #00000073;
        --md-scrollbar-bg-color: #0f0f0f;
        --md-scrollbar-thumb-color: #2d2d2d;
        --md-scrollbar-thumb-hover-color: #3a3a3a;
        --md-scrollbar-thumb-active-color: #3a3a3a;
    }
</style>
