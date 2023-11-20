<script lang="ts">
    import { useMessage } from 'naive-ui'
    import { defineComponent } from 'vue'

    export default defineComponent({
        setup () {
            window.$message = useMessage() // 关键代码
        }
    })
</script>

<template>

</template>

<style lang="scss" scoped>
</style>
