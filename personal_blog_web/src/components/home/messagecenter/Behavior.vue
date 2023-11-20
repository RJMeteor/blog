<script setup lang="ts">
    import {ref} from "vue"

    const readerState = ref<boolean>(false)
    const active = ref<boolean>(false)
    const readerStateCallback = ref<Function>(()=>{})
    const activeCallback = ref<Function>(()=>{})
    defineExpose({
        readerState,
        active,
        readerStateCallback,
        activeCallback
    })
</script>

<template>

    <n-button-group>
        <n-button v-if="readerState" @click="readerStateCallback">
            全部已读
        </n-button>
        <n-button v-if="active" @click="activeCallback">
            全部删除
        </n-button>
    </n-button-group>

</template>

<style lang="scss" scoped>

</style>
