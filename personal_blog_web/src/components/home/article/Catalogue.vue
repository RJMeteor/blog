<script setup lang="ts">
    import {useScroll, watchThrottled} from '@vueuse/core';
    import {ref, onMounted, nextTick} from "vue"

    const titleList = ref<any>([]); //获取所有H标签的文本内容
    const currentIndex = ref(0);    //当前观看部分所属的目录下标
    const props = defineProps({     //domRef是要被生成目录的文章的ref
        domRef: {
            type: Object,
            default: null,
        }
    });

    const getTitles = () => {
        // const parser = new DOMParser()
        // let document = parser.parseFromString(props.domRef, "text/xml");
        const anchors = props.domRef.$el.querySelectorAll('h1,h2,h3')
        // const titles = Array.from(anchors).filter((t: any) => !!t.innerText.trim())
        const titles = Array.from(anchors)
        if (!titles.length)
            titleList.value = []
        const hTags = Array.from(new Set(titles.map((t: any) => t.tagName))).sort()
        titleList.value = titles.map((el: any, idx: number) => {
            return {
                title: el.innerText,
                lineIndex: el.getAttribute('data-v-md-line'),
                indent: hTags.indexOf(el.tagName),
            }
        })
    }

    function handleAnchorClick(anchor: any, idx: number) {
        // const parser = new DOMParser()
        // let document = parser.parseFromString(props.domRef, "text/html");
        const heading = props.domRef.$el.querySelector(`[data-v-md-line="${anchor.lineIndex}"]`)
        if (heading) {
            window.scrollTo({
                behavior: 'smooth',
                //@ts-ignore
                top: heading.offsetTop - 40,
            })
            setTimeout(() => currentIndex.value = idx, 600)
        }
    }

    const {y} = useScroll(window)
    watchThrottled(y, () => {
        titleList.value.forEach((e: any, idx: number) => {
            // const parser = new DOMParser()
            // let document = parser.parseFromString(props.domRef, "text/html");
            const heading = props.domRef.$el.querySelector(`[data-v-md-line="${e.lineIndex}"]`)
            //@ts-ignore
            if (y.value >= heading.offsetTop - 50) // 比 40 稍微多一点
                currentIndex.value = idx


        })
    }, {throttle: 200})
    onMounted(() => {
        console.log(props.domRef)
        nextTick(() => {
            getTitles();
        })
    });


</script>
<template>
    <div class="catalog-header">
        <div style="margin-left: 5px;">目录</div>
    </div>
    <div class="catalog-content">
        <div class="catalog-item" v-for="(anchor, index) of titleList" :key="anchor.title"
             :class="currentIndex === index ? 'active' : ''" :style="{ paddingLeft: `${5 + anchor.indent * 15}px` }"
             @click="handleAnchorClick(anchor, index)">
            <a> {{ anchor.title }} </a>
        </div>
    </div>
</template>

<style lang="scss" scoped>
    .catalog-header {
        display: flex;
        align-items: center;
    }
    .catalog-content {
        max-height: calc(100vh - 100px);
        overflow: auto;
        margin-right: -16px;
        padding-right: 16px;
    }

    .catalog-item {
        margin: 5px 0;
        cursor: pointer;
        transition: all 0.2s ease-in-out;
        font-size: 14px;
        padding: 2px 6px;
        overflow: hidden;
        text-overflow: ellipsis;

        &:hover {
            color: #e9546b;
        }
    }

    .active {
        background-color: #e9546b;
        color: #fff;

        &:hover {
            background-color: #49b1f5;
            color: #fff;
        }
    }
</style>

