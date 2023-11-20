<script setup lang="ts">
    import {ref,onMounted} from "vue"
    import Sider from "./Sider.vue"

</script>

<template>
    <div id="index_box">
        <n-layout>
            <n-layout has-sider>
                <Sider/>
                <n-layout-content>
                    <RouterView v-slot="{ Component }">
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
    </div>
</template>

<style lang="scss" scoped>
       #index_box {
        width: 100vw;
        height: 100vh;
        min-width: 600px;
        min-height: 600px;
        .n-layout-sider {
            padding: 0;
            height: 100vh;
        }
        .n-layout-content {
            padding: 0;
            height: 100vh;
            width: calc(100vw - 100px);
        }
    }
</style>
