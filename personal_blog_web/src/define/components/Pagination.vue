<script setup lang="ts">
    import {ref,defineExpose} from "vue"
    //每页长度定义
    const pageSizes = ref<Array<number>>([10, 20, 30, 40])
    //第几页
    const page = ref<number>(1)
    //总条数
    const itemCount = ref<number>(10)
    //每页长度
    const pageSize = ref<number>(pageSizes.value[0])

    const emit = defineEmits(['pageChange'])

    function pageChange(pageIndex:number) {
        emit('pageChange')
    }

    function pageSizeChange(size:number) {
        emit('pageChange')
    }
    defineExpose({
        page,
        pageSize,
        itemCount
    })
</script>

<template>
    <n-pagination
            v-model:page="page"
            v-model:page-size="pageSize"
            @update:page="pageChange"
            @update:page-size="pageSizeChange"
            :item-count="itemCount"
            show-size-picker
            :page-sizes="pageSizes"
    />
</template>

<style lang="scss">

</style>
