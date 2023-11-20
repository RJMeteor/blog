<script setup lang="ts">
    import {PricetagsOutline, TrashOutline, SparklesSharp, EyeOutline} from "@vicons/ionicons5"
    import {h, onMounted, ref, nextTick, watch,onActivated} from "vue";
    import {DropdownOption, NIcon} from "naive-ui";
    import {useRouter, useRoute} from "vue-router"
    import {articleAsUserId, deleteArticles} from "../../hook/request/creation.request";
    import {User, UserInfo} from "../../define/user";
    import {Articles} from "../../define/creation.request";
    import Modal from "../../define/components/Modal.vue"
    import {informationView} from "../../hook/request/user.request";
    import {Attention} from "../../define/attention";
    import {attentionRequest} from "../../hook/request/fansAndAttention.request";
    import blockquote from "naive-ui/es/typography/src/blockquote";
    import {favoriteNameAsCount} from "../../hook/request/favorite.request";

    const modal = ref<typeof Modal>(null)

    const router = useRouter()
    const route = useRoute()

    function handleBack() {
        router.back()
    }

    const userInfo = ref<UserInfo>()
    const articles = ref<Array<Articles>>([])
    const attentionByids = ref<Array<number>>()

    const pages = ref<number>(1)
    const sizes = ref<number>(0)
    const loadingStatus = ref<boolean>(false)
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

    const observers = new IntersectionObserver((entries, observer) => {
        for (let entry of entries) {
            if (entry.isIntersecting) {
                testFun(function () {
                    beforeRequest(pages.value, sizes.value)
                })
            }
        }
    })

    onMounted(function () {
        nextTick(function () {
            sizes.value = Math.ceil(document.documentElement.clientHeight / 150)
            beforeRequest(pages.value, sizes.value)
        })
    })

    function beforeRequest(page: number, size: number) {
        console.log(route.params.id)
        articleAsUserId(+route.params.id, page, size).then(res => {
            if (!res.data.records.length) {
                informationView(+route.params.id).then(res => {
                    userInfo.value = res.data
                    attentionByids.value = res.data?.attentionAsId
                })
                loadingStatus.value =false
            } else {
                if (res.code == 200) {
                    if (res.data.records.length != 0) {
                        userInfo.value = res.data.records[0]?.blogUserCover
                        articles.value.push(...res.data.records)
                        attentionByids.value = res.data.records[0]?.blogUserCover?.attentionAsId
                        pages.value = res.data.current + 1
                        sizes.value = res.data.size
                        loadingStatus.value = Math.ceil(res.data.total / sizes.value) >= pages.value
                    } else {
                            (document.getElementById("loading") as HTMLElement).remove()
                            observers.disconnect()
                        loadingStatus.value =false
                    }
                }
            }
        })
    }
    watch(loadingStatus, value => {
        if (value) {
            nextTick(function () {
                if (document.getElementById("loading")) {
                    observers.observe(document.getElementById("loading") as HTMLElement)
                }
            })
        }
    })
    function readerArticle(articleId: number) {
        router.push({
            path: `/reader/${articleId}`
        })
    }

    const user: User = JSON.parse(localStorage.getItem('user') as string);

    function isAttention(userId: number): boolean {
        if (user.id != userId) {
            if (!attentionByids.value?.includes(user.id as number)) {
                return true
            }
        }
        return false
    }

    function viewPrivateLetter(userId: number) {
        modal.value.showModal = true
        modal.value.userId = userId
    }

    function isPrivateletter(userId: number): boolean {
        if (user.id != userId) {
            if (attentionByids.value?.includes(user.id as number)) {
                return true
            }
        }
        return false
    }

    function attention(attentionId: number) {
        const att: Attention = {
            userId: user.id,
            attentionUserId: attentionId
        }
        attentionRequest(att).then(res => {
            if (res.code == 200) {
                beforeRequest(pages.value, sizes.value)
            }
        })
    }

    function deleteArticle(e: Event, articleId: number) {
        e.preventDefault()
        e.stopPropagation()
        deleteArticles(articleId, user.id as number).then(res => {
            if (res.code == 200) {
                for (let index in articles.value) {
                    if (articles.value[index].blogArticleContentMapping?.article?.id == articleId) {
                        articles.value.splice(+index, 1)
                    }
                }
            }
        })

    }

    function updateArticle(e: Event, articleId: number) {
        e.preventDefault()
        e.stopPropagation()
        router.push({
            path: `/edit/${articleId}`
        })
    }

    function favoriteArticle(e: Event, articleId: number) {
        e.preventDefault()
        e.stopPropagation()
    }

    function isSelf(userId: number): boolean {
        return user.id == userId
    }
</script>

<template>
    <Modal ref="modal"/>
    <div id="homepage">
        <n-scrollbar>
            <n-layout>
                <n-layout-header>
                    <n-page-header style="color: white" subtitle="返回" @back="handleBack">
                    </n-page-header>
                </n-layout-header>
                <n-layout-content content-style="padding: 24px;">
                    <n-list>
                        <n-list-item>
                            <template #prefix>
                                <n-avatar
                                        round
                                        :size="100"
                                        :src="userInfo?.blogUser?.userImage?userInfo?.blogUser?.userImage:'/icon.png'"
                                />

                            </template>
                            <template #suffix>
                                <n-button v-if="isAttention(userInfo?.blogUser?.id)" strong secondary
                                          @click="attention(userInfo?.blogUser?.id)"
                                          type="primary">关注
                                </n-button>
                                <n-button v-if="isPrivateletter(userInfo?.blogUser?.id)" strong secondary
                                          @click="viewPrivateLetter(userInfo?.blogUser?.id)"
                                          type="tertiary">私信
                                </n-button>
                            </template>
                            <n-thing :title="userInfo?.blogUser?.userName">
                                <template #description>
                                    <n-space size="small" style="margin-top: 4px">
                                        <n-tag :bordered="false" type="info" size="small">
                                            粉丝：{{userInfo?.fans}}
                                        </n-tag>
                                        <n-tag :bordered="false" type="info" size="small">
                                            关注：{{userInfo?.attention}}
                                        </n-tag>
                                    </n-space>
                                </template>
                                {{userInfo?.blogUser?.userIntro}}
                            </n-thing>
                        </n-list-item>
                    </n-list>
                </n-layout-content>
            </n-layout>

            <n-divider>
                文章
            </n-divider>
            <div id="wenzhang">
                <n-thing v-for="ele in articles" @click="readerArticle(ele.blogArticleContentMapping?.article?.id)">
                    <template #header>
                        {{ele.articleTitle}}
                    </template>
                    <template #header-extra>
                        {{ele.createTime}}
                    </template>
                    <n-ellipsis :tooltip="false" :line-clamp="3">
                        {{ele.blogArticleContentMapping?.articleContentNotHtml}}
                    </n-ellipsis>
                    <template #footer>
                        <n-space>
                            <span> 浏览数：{{ele.browse}}</span>
                            <span>
                                点赞数：{{ele.like}}
                           </span>
                        </n-space>
                    </template>
                    <template #action>
                        <!--                        <n-space>-->
                        <n-button-group>
                            <n-button round v-if="isSelf(userInfo?.blogUser?.id)"
                                      @click="deleteArticle($event,ele.blogArticleContentMapping?.article?.id)"
                                      type="error" size="small">
                                <template #icon>
                                    <n-icon>
                                        <TrashOutline/>
                                    </n-icon>
                                </template>
                                删除
                            </n-button>
                            <n-button round v-if="isSelf(userInfo?.blogUser?.id)"
                                      @click="updateArticle($event,ele.blogArticleContentMapping?.article?.id)"
                                      type="info" size="small">
                                <template #icon>
                                    <n-icon>
                                        <PricetagsOutline/>
                                    </n-icon>
                                </template>
                                编辑
                            </n-button>
                            <!--                            <n-button @click="browseArticle(ele.blogUser?.id)" type="primary" size="small">-->
                            <!--                                <template #icon>-->
                            <!--                                    <n-icon>-->
                            <!--                                        <EyeOutline/>-->
                            <!--                                    </n-icon>-->
                            <!--                                </template>-->
                            <!--                                浏览-->
                            <!--                            </n-button>-->
                            <!--                            <n-button @click="favoriteArticle($event,ele.blogArticleContentMapping?.article?.id)" size="small">-->
                            <!--                                <template #icon>-->
                            <!--                                    <n-icon>-->
                            <!--                                        <SparklesSharp/>-->
                            <!--                                    </n-icon>-->
                            <!--                                </template>-->
                            <!--                                收藏-->
                            <!--                            </n-button>-->
                        </n-button-group>
                        <!--                        </n-space>-->
                    </template>
                </n-thing>
            </div>
            <div v-show="loadingStatus" id="loading">
                <img src="/Skateboarding.gif">
            </div>
            <p style="text-align: center;color:#ddd;" v-show="!loadingStatus">没有更多了.....</p>
            <n-back-top :right="100">
            </n-back-top>
        </n-scrollbar>
    </div>
</template>

<style lang="scss" scoped>
    #loading {
        width: 100%;
        display: flex;
        justify-content: center;
        margin-bottom: 20px;
    }

    #homepage {
        width: 100vw;
        height: 100vh;

        .n-card {
            width: 400px;
        }

        .n-list-item .n-button {
            height: 100px;
        }

        .n-scrollbar .n-layout-content .n-list-item {
            width: 600px;
            margin: 0 auto;
        }

        .n-scrollbar .n-list-item {
            width: 100%;
        }

        .n-layout-header {
            height: 45px;
            display: flex;
            align-items: center;
        }

        #wenzhang {
            margin: 0px 50px 0px 50px;
        }

        .n-avatar {
            cursor: pointer;
            background-color: white !important;
        }

    }

    .n-thing {
        padding: 50px;

        .n-thing-main:hover {
            box-shadow: 0 0 10px rgb(239, 239, 245);
        }
    }

    #wenzhang {
        .n-thing {
            cursor: pointer;
            margin: 10px 0px;
            border: 2px solid #f4f5f5;
        }
    }

    ::v-deep(.n-ellipsis) {
        width: calc(100vw - 180px) !important;
    }
</style>
