<script setup lang="ts">
    import {ref, reactive, onMounted, nextTick, watch} from "vue"
    import {articles} from "../../../hook/request/creation.request"
    import {Articles} from "../../../define/creation.request";
    import {useRouter} from "vue-router"
    import hr from "naive-ui/es/typography/src/hr";

    const articleList = ref<Array<Articles>>([])
    const router = useRouter()

    const seacher = reactive({
        value: ""
    })

    const pages = ref<number>(1)
    const sizes = ref<number>(0)
    const loadingStatus = ref<boolean>(false)

    onMounted(function () {
        nextTick(function () {
            sizes.value = Math.ceil(document.documentElement.clientHeight / 150)
            articles(pages.value, sizes.value).then(res => {
                if (res?.code == 200) {
                    articleList.value!.push(...res.data.records)
                    pages.value = res.data.current + 1
                    sizes.value = res.data.size
                    loadingStatus.value = Math.ceil(res.data.total / sizes.value) >= pages.value
                }
            })
        })
    })

    function readerArticle(articleId: number) {
        //  let href = router.resolve({
        //      path:`/reader/${articleId}`
        //  }).href;
        // window.open(href,"_blank")
        router.push({
            path: `/reader/${articleId}`
        })
    }

    function personalHomepage(e: Event, userId: number) {
        e.preventDefault()
        e.stopPropagation()
        router.push({
            path: `/homepage/${userId}`
        })
    }

    let timeout: any = null
    //防抖，只请求最后一次操作
    const testFun = (fun: Function, time = 1500) => {
        if (timeout) {
            clearTimeout(timeout) // 规定时间内若定时器存在则清除
        }
        timeout = setTimeout(() => {
            fun() // 调用接口
        }, time);
    }
    const observer = new IntersectionObserver((entries, observer) => {
        for (let entry of entries) {
            if (entry.isIntersecting) {
                testFun(function () {
                    articles(pages.value, sizes.value).then(res => {
                        if (res?.code == 200) {
                            if (res.data.records.length != 0) {
                                articleList.value!.push(...res.data.records)
                                pages.value = res.data.current + 1
                                sizes.value = res.data.size
                                loadingStatus.value = Math.ceil(res.data.total / sizes.value) >= pages.value
                            } else {
                                (document.getElementById("loading") as HTMLElement).remove()
                                observer.disconnect()
                            }
                        }
                    })
                })
            }
        }
    })
    watch(loadingStatus, value => {
        if (value) {
            nextTick(function () {
                if (document.getElementById("loading")) {
                    observer.observe(document.getElementById("loading") as HTMLElement)
                }
            })
        }
    })

    function handleValidateClick() {
        articleList.value = []
        pages.value = 1
       loadingStatus.value = false
        articles(pages.value, sizes.value, seacher.value).then(res => {
            if (res?.code == 200) {
                articleList.value!.push(...res.data.records)
                pages.value = res.data.current + 1
                sizes.value = res.data.size
                loadingStatus.value = Math.ceil(res.data.total / sizes.value) >= pages.value
            }
        })
    }
</script>

<template>
    <div id="article">
        <n-form
                ref="formRef"
                inline
                :label-width="80"
                :model="seacher"
                :size="size"
        >
            <n-form-item path="user.name">
                <n-input v-model:value="seacher.value" placeholder="SEACHER"/>
            </n-form-item>
            <n-form-item>
                <n-button attr-type="button" @click="handleValidateClick">
                    查询
                </n-button>
            </n-form-item>
        </n-form>

        <n-list v-for="(ele,index) in articleList" @click="readerArticle(ele.blogArticleContentMapping?.article?.id)"
                hoverable
                bordered clickable>
            <n-list-item>
                <n-thing>
                    <template #avatar>
                        <n-avatar
                                round
                                :size="25"
                                @click="personalHomepage($event,ele.blogUser?.id)"
                                :src="ele?.blogUser?.userImage?ele?.blogUser?.userImage:'/icon.png'"
                        />
                    </template>
                    <template #header-extra>
                        {{ele.createTime}}
                    </template>
                    <template #header>
                        {{ele.articleTitle}}
                    </template>
                    <template #footer>
                        <n-space>
                            <span> 浏览数：{{ele.browse}}</span>
                            <span>
                                点赞数：{{ele.like}}
                           </span>
                        </n-space>
                    </template>

                    <n-ellipsis :tooltip="false" :line-clamp="3">
                        {{ele.blogArticleContentMapping?.articleContentNotHtml}}
                    </n-ellipsis>
                </n-thing>
            </n-list-item>
        </n-list>
        <div v-show="loadingStatus" id="loading">
            <img src="/Skateboarding.gif">
        </div>
        <p style="text-align: center;color:#ddd;" v-show="!loadingStatus">没有更多了.....</p>
    </div>
    <n-back-top :right="100">
    </n-back-top>
</template>

<style lang="scss" scoped>
    #article {
        padding: 0px 20px;
    }

    .n-list {
        margin: 10px 0px;
    }

    ::v-deep(.n-ellipsis) {
        width: calc(100vw - 392px) !important;
    }

    #loading {
        width: 100%;
        display: flex;
        justify-content: center;
        margin-bottom: 20px;
    }
</style>
