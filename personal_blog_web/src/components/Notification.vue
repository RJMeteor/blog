<script lang="ts">
    import { useNotification } from 'naive-ui'
    import { defineComponent } from 'vue'

    export default defineComponent({
        setup () {
            window.$notification=useNotification() // 关键代码
        }
    })
</script>

<template>

</template>

<style lang="scss" scoped>
</style>
