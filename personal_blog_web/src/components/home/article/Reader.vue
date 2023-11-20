<script setup lang="ts">
    import {ref, watch,reactive, h, onMounted, Component, Ref, nextTick} from "vue";
    import {MdPreview, MdCatalog, HeadList} from 'md-editor-v3';
    import {
        DropdownOption,
        lightTheme,
        NAvatar,
        NButton,
        NCollapse,
        NCollapseItem,
        NImage,
        NThing,
        TreeOption
    } from "naive-ui";
    import {useRoute} from "vue-router"
    import {readArticle, sendComment, getComment, doBrowseAsArticle} from "../../../hook/request/creation.request";
    import {rollupVersion} from "vite";
    import {HeartOutline, StarOutline, Star, ChatboxEllipsesOutline, HeartSharp} from "@vicons/ionicons5"
    import {Articles} from "../../../define/creation.request";
    import {Comment} from "../../../define/messagecenter";
    import {
        addFavoriteAsArticle,
        deleteFavoriteAsArticle,
        deleteLikeAsArticle,
        doLikeAsArticle,
        favoriteNameAsCount
    } from "../../../hook/request/favorite.request";
    import {User} from "../../../define/user";
    import {Favorite, FavoriteNameCount} from "../../../define/favorite";
    import {useRouter} from "vue-router"
    import {
        generateComments,
        personUserId,
        sendCommentKeyId,
        commentPlaceholder,
        concurrentComment,
        commentContent
    } from "../../../hook/handler/generateMultiLevelComments";
    import {Like} from "../../../hook/correspondence/sse";
    import {
        exportGenerateDirectory,
        storeRenderingDirectory,
        endEntry
    } from "../../../hook/handler/generateArticleDirectoryStructure";

    const route = useRoute()

    //提供md字符串得到目录结构
    //提供hmtl字符串嵌入html
    const creationComment = ref<string>()
    const scrollElement = document.getElementsByClassName("n-scrollbar-content")[0];

    /*
    * 文章
    * */
    let article = ref<Articles>()
    /*
    * 评论
    * */
    let commentList = ref<Array<Comment>>([])
    let commentCount = ref<number>(0)
    /*
    * 该文章是否被收藏
    * */
    const isFavorite = ref<boolean>(false)
    /*
    * 该文章是否被点赞
    * */
    const isLike = ref<boolean>(false)


    /*
    * 评论的模拟框状态
    * */
    const commentModalStatus = ref<boolean>(false)
    watch(commentModalStatus,value => {
        if (!value){
            resetSend()
        }
    })

    /*
    * 评论元素组件
    * */
    const defineSelfComponent = ref<Component>()

    /*
    * 下拉收藏列表框
    * */
    const favorites = ref<Array<DropdownOption>>([])
    const html = ref<string>()

    const router = useRouter()

    const user: User = JSON.parse(localStorage.getItem("user") as string)
    onMounted(function () {
        readArticle(+route.params.id, user.id as number).then(res => {
            article.value = res.data
            personUserId.value = article.value?.blogUser?.id as number
            isFavorite.value = article.value?.isFavorite
            isLike.value = article.value?.isLike
            commentCount.value = article.value?.commentCount
            html.value = article.value?.blogArticleContentMapping?.articleContent
            commentList.value = !(res.data.blogCommentList as Array<Comment>) ? [] : res.data.blogCommentList as Array<Comment>

            defineSelfComponent.value = h(NCollapse,
                null,
                () => generateComments(commentList.value as Array<Comment>))
            creationComment.value = res.data.blogArticleContentMapping?.mdContent
            favoriteNameAsCount(user.id as number).then(res => {
                for (let datum of res.data) {
                    favorites.value.push({
                        label: datum.favoriteName,
                        key: datum.favoriteName
                    })
                }
            })
            if (article.value?.blogUser?.id != user.id) {
                doBrowseAsArticle({
                    personId: article.value?.blogUser?.id as number,
                    userId: user.id as number,
                    articleId: article.value?.blogArticleContentMapping?.article?.id as number,
                })
            }
            resetSend()
        })
    })


    function onGetCatalog(list: HeadList[]): void {

        exportGenerateDirectory()
    }

    //返回上一个历史记录
    function handleBack() {
        router.back()
    }

    function handleFavorite(key: string | number) {
        //@ts-ignore
        const sendFavorite: Favorite = {
            userId: user.id as number,
            articleId: article.value?.blogArticleContentMapping?.article?.id as number
        }
        if (isFavorite.value) {
            deleteFavoriteAsArticle(sendFavorite).then(res => {
                if (res.code == 200) {
                    isFavorite.value = false
                }
            })
        } else {
            addFavoriteAsArticle({
                //@ts-ignore
                favoriteName: key as string,
                ...sendFavorite
            }).then(res => {
                if (res.code == 200) {
                    isFavorite.value = true
                }
            })
        }

    }

    /*
     * 发送评论消息
     * */
    function sendMessage(concurrentComment: Comment) {
        const comment: Comment = {
            personId: (personUserId.value == user.id) ? 0 : personUserId.value,
            userId: user.id as number,
            //@ts-ignore
            articleId: article.value?.blogArticleContentMapping?.article?.id as number,
            commentContentMapping: {
                //@ts-ignore
                commentContent: commentContent.value
            },
            commentKeyId: sendCommentKeyId.value
        }

        sendComment(comment).then(res => {
            if (res.code == 200) {
                //@ts-ignore
                if (concurrentComment) {
                    concurrentComment.isChildren = true
                    concurrentComment.children.push(res.data)
                } else {
                    commentList.value.push(res.data)
                }
                window.$message.success("评论成功")
            }
        })
        resetSend()
    }

    /*
    * 重置回复评论的初始Id
    * */
    function resetSend() {
        personUserId.value = article.value?.blogUser?.id as number
        sendCommentKeyId.value = 0
        commentPlaceholder.value = "输入评论内容...."
        concurrentComment.value = undefined
        commentContent.value = ""
    }


    /*
    * 点赞
    * */
    function doLike() {
        const sendLike = {
            userId: user.id as number,
            personId: article.value?.blogUser?.id,
            articleId: article.value?.blogArticleContentMapping?.article?.id as number
        }
        if (isLike.value) {
            deleteLikeAsArticle(sendLike.userId, sendLike.articleId).then(res => {
                if (res.code == 200) {
                    isLike.value = false
                }
            })
        } else {
            doLikeAsArticle(sendLike).then(res => {
                if (res.code == 200) {
                    isLike.value = true
                }
            })
        }
    }

    function moveEle(e: Event, el: Element) {
        e.stopPropagation()
        e.preventDefault()
        el.scrollIntoView({
            block: "start",
            // behavior: "smooth"
        })
        endEntry.value = el
    }
</script>

<template>
    <!--    目的时得到目录结构-->
    <div v-show="false">
        <n-scrollbar>
            <MdPreview
                    :modelValue="creationComment"
                    :theme="'light'"
                    @onGetCatalog="onGetCatalog"/>
        </n-scrollbar>
    </div>
    <!--    文章内容渲染-->
    <div id="reader">
        <n-layout>
            <n-layout has-sider>
                <n-layout-sider
                        bordered
                        :width="240"
                        :native-scrollbar="false"
                >
                    <div class="layout-sider">
                        <n-layout-header>
                            <n-page-header @back="handleBack">
                                <template #avatar>
                                    {{article?.articleTitle}}
                                </template>
                                <template #title>
                                    <n-space>
                                        <n-icon>
                                            <EyeOutline/>
                                        </n-icon>

                                    </n-space>
                                </template>
                            </n-page-header>
                        </n-layout-header>
                        <n-layout-content>
                            <n-scrollbar>
                                <div class="dirTitleBox" v-for="ele in storeRenderingDirectory">
                                    <div class="dirTitleList" :style="{
                                            borderLeftColor:ele.activeColor,
                                         borderLeftWidth:'4px',
                                         color:ele.activeColor,
                                         borderLeftStyle:'smail'}"
                                         @click="moveEle($event,ele.element)" :data-key="'key'+ele.key">
                                        {{ele.titleName}}
                                    </div>
                                    <div class="dirTitleList" :data-key="'key'+el.key" v-for="el in ele.childers"
                                         @click="moveEle($event,el.element)"
                                         :style="{paddingLeft:el.leval*50+'px',
                                         borderLeftColor:el.activeColor,
                                         color:el.activeColor,
                                         borderLeftWidth:'4px',
                                         fontSize:24-el.leval*8+'px'}">
                                        {{el.titleName}}
                                    </div>
                                </div>
                            </n-scrollbar>
                        </n-layout-content>
                        <n-layout-footer>
                            <n-card>
                                <template #footer>
                                    <n-space>
                                        <n-dropdown v-if="!isFavorite" trigger="hover" :options="favorites"
                                                    @select="handleFavorite">
                                            <n-icon size="25" :depth="4">
                                                <StarOutline/>
                                            </n-icon>
                                        </n-dropdown>
                                        <n-icon v-if="isFavorite" size="25" @click="handleFavorite" color="#fcb040">
                                            <Star/>
                                        </n-icon>
                                        <n-icon size="25" v-if="!isLike" @click="doLike" :depth="4">
                                            <HeartOutline/>
                                        </n-icon>
                                        <n-icon size="25" v-if="isLike" @click="doLike" color="red">
                                            <HeartSharp/>
                                        </n-icon>

                                        <n-tag @click="commentModalStatus=!commentModalStatus" round :bordered="false"
                                               type="success">
                                            {{commentCount>99?"99+":commentCount}}
                                            <template #icon>
                                                <n-icon size="20">
                                                    <ChatboxEllipsesOutline/>
                                                </n-icon>
                                            </template>
                                        </n-tag>

                                    </n-space>
                                </template>
                            </n-card>
                        </n-layout-footer>
                    </div>
                </n-layout-sider>
                <n-layout-content>
                    <div id="readerComment">
                        <h1 id="title">{{article?.articleTitle}}</h1>
                        <div data-v-09e8c3fe="" id="my-editor" class="md-editor md-editor-previewOnly">
                            <div id="my-editor-preview-wrapper" class="md-editor-preview-wrapper">
                                <article id="my-editor-preview" class="md-editor-preview default-theme wenzhangComment"
                                         v-html="html"/>
                            </div>
                        </div>
                    </div>
                </n-layout-content>
            </n-layout>
        </n-layout>
    </div>
    <!--    评论模拟框-->
    <n-drawer v-model:show="commentModalStatus" :width="502" placement="right">
        <n-drawer-content>
            <template #header>
                评论
            </template>
            <template #footer>
                <div id="inputSend">
                    <n-input
                            v-model:value="commentContent"
                            :placeholder="commentPlaceholder"
                            type="textarea"
                            size="small"
                            clearable
                            :autosize="{
                        minRows: 5,
                        maxRows: 5
                      }"
                    />
                    <n-button id="rButton" @click="sendMessage(concurrentComment)" type="primary">
                        发送
                    </n-button>
                    <n-button id="rButton" @click="resetSend">
                        重置回复
                    </n-button>
                </div>
            </template>
            <defineSelfComponent/>
        </n-drawer-content>
    </n-drawer>
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


    #title {
        padding: 0px 20px;
    }

    #reader {
        height: 100vh;
        width: 100vw;
        display: flex;
    }

    #readerComment {
        height: 100vh;
        overflow-y: auto;
    }

    .layout-sider {
        height: 100vh;

        .n-layout-content {
            background-color: #f4f5f5;
            --n-item-color-hover: white;
            padding: 0;
            width: 100%;
            height: calc(100vh - 120px);
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

                & > :nth-child(1) {
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

    #inputSend {
        width: 100%;
        display: flex;

        .n-button {
            align-self: flex-end;
            margin-left: 10px;
        }
    }

    .n-card {
        background-color: #f4f5f5;
    }

    ::v-deep(.n-thing-header__title) {
        font-size: 12px !important;
    }

    ::v-deep(.n-collapse-item) {
        margin-left: 0 !important;
    }

    ::v-deep(.n-thing-main__description) {
        font-size: 12px !important;
    }

    ::v-deep(.n-thing-header-wrapper) {
        width: 370px !important;
        display: flex;
        flex-direction: column;
        justify-content: center;
    }

    ::v-deep(.n-thing-header__extra) {
        font-size: 12px !important;
    }

    ::v-deep(.n-avatar) {
        background-color: white;
    }

    .dirTitleList {
        padding: 5px 10px;
        font-size: 12px;
        font-family: "粗体";
        border-left: 1px solid transparent;
    }

    .dirTitleBox {
        margin-left: 20px;
        margin-right: 20px;
    }
</style>
