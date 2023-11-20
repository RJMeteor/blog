<script setup lang="ts">
    import {DataTableRowKey, NButton, NIcon, NSpace, NTooltip} from "naive-ui"
    import {ChatbubbleEllipsesSharp, Trash} from "@vicons/ionicons5"
    import {
        FlashOutline,
        AddCircleOutline,
        PawOutline,
        Close,
        EllipsisHorizontalSharp,
        TrashOutline,
        PricetagsOutline, SearchOutline
    } from "@vicons/ionicons5"
    import {ref, reactive, h, onMounted} from "vue"
    import {renderIcon} from "../../../hook/handler/icon";
    import Pagination from "../../../define/components/Pagination.vue"
    import Button from "naive-ui/es/button/src/Button";
    import {
        addFavorite,
        deleteAsFavorite, deleteInFavoriteAsArticle, deletetFavoriteAsArticle,
        editAsFavorite,
        favoriteNameAsCount,
        favoriteNameList
    } from "../../../hook/request/favorite.request";
    import {User} from "../../../define/user";
    import {Favorite, FavoriteNameCount} from "../../../define/favorite";
    import {useRouter} from "vue-router";


    type RowType = {
        key?: number
        articleName?: string
        userName?: string
        createTime?: string
    }

    //弹出添加收藏夹状态
    const stats = ref<boolean>(false)

    const addFavoriteName = ref<string>("")

    //添加收藏夹函数
    function addCloseModal() {
        if (addFavoriteName.value.trim().length) {
            const user: User = JSON.parse(localStorage.getItem("user") as string)
            addFavorite(user?.id, addFavoriteName.value).then(res => {
                if (res.code == 200) {
                    beforRequestFavoriteNameAsCount(user?.id)
                }
            })
            addFavoriteName.value = ""
            stats.value = false
        } else {
            window.$message.info("文件夹名不可以为空")
        }
    }

    //单个收藏夹事件函数
    function cardSelect(key: string) {
        console.log(key)
    }

    //目的是固定表头，滑动内容
    const tableHeight = ref<number>(document.body.offsetHeight - 110)

    const selectRow  = ref<Array<number>>([])
    //处理选中的行
    function handleCheck(e: MouseEvent) {
        selectRow.value = [...(e as any)]
    }

    const clickEventFavoriteName = ref<string>()
    //数据表格的表头
    const columns = [
        {
            type: 'selection',
        },
        {
            title: '文章内容',
            key: 'articleName'
        },
        {
            title: '作者',
            key: 'userName'
        },
        {
            title: '收藏时间',
            key: 'createTime',
            filter: 'default',
            filterOptionValue: null,
            renderFilterIcon: () => {
                return h(NTooltip, {
                    trigger: "hover", placement: "bottom",
                    style: {
                        color: "#000",
                        background: "white"
                    },
                }, {
                    trigger: () => h(NIcon, {
                            color: "red",
                            onClick() {
                                deleteInFavoriteAsArticle(selectRow.value).then(res=>{
                                    if (res.code == 200) {
                                        const user: User = JSON.parse(localStorage.getItem("user") as string)
                                        beforRequestFavoriteNameList(user, clickEventFavoriteName.value as string)
                                    }
                                })
                            }
                        },
                        {default: () => h(TrashOutline)}),
                    default: () => "删除选中的行"
                })
            },
            renderFilterMenu: () => {
            }
        },
    ]
    //表格数据来源
    const data = ref<Array<RowType>>([])
    //收藏夹列表
    const favoriteList = ref<Array<FavoriteNameCount>>([])


    //定义选中的行的返回值
    const rowKey = (row: RowType) => row.key

    const user: User = JSON.parse(localStorage.getItem("user") as string)
    onMounted(function () {
        const user: User = JSON.parse(localStorage.getItem("user") as string)
        beforRequestFavoriteNameAsCount(user.id as number)
        beforRequestFavoriteNameList(user, "默认收藏夹")
        clickEventFavoriteName.value = "默认收藏夹"
    })

    function beforRequestFavoriteNameAsCount(id: number) {
        favoriteList.value.length = 0
        favoriteNameAsCount(id).then(res => {
            favoriteList.value.push(...res.data)
        })
    }

    const paginationRef = ref<{
        page?:number,
        pageSize?:number,
        itemCount?:number
    }>({})

    const favoriteToarticleMap = new Map<Number,Number>()
    function beforRequestFavoriteNameList(user: User, favoriteName: string) {
        let {
            page,
            pageSize,
            itemCount
        } = paginationRef.value
        data.value.length = 0
        favoriteNameList(user.id as number, favoriteName, page as number, pageSize as number).then(res => {
            const apply: Array<RowType> = []
            paginationRef.value.page = res.data.current
            paginationRef.value.pageSize = res.data.size
            paginationRef.value.itemCount = res.data.total
            for (let ele of res.data.records) {
                if (ele.article?.articleTitle) {
                    favoriteToarticleMap.set(ele?.id as number,ele.article?.blogArticleContentMapping?.article?.id as number)
                    apply.push({
                        key: ele.id,
                        userName: ele.article?.blogUser?.userName,
                        articleName: ele.article?.articleTitle,
                        createTime: ele.createTime
                    })
                }
            }
            data.value.push(...apply)
        })
    }



    function findFiles(favoriteName: string) {
        selectRow.value = []
        clickEventFavoriteName.value = favoriteName
        const user: User = JSON.parse(localStorage.getItem("user") as string)
        beforRequestFavoriteNameList(user, favoriteName)
    }

    const editFavoriteName = ref<string>("")
    const oldFavoriteName = ref<string>("")
    const editStats = ref<boolean>(false)

    function editFavorite(e: Event, favoriteName: string) {
        e.stopPropagation()
        e.preventDefault()
        editFavoriteName.value = favoriteName;
        oldFavoriteName.value = favoriteName
        editStats.value = true
    }

    function editCloseModal() {
        if (editFavoriteName.value.trim().length) {
            editAsFavorite(oldFavoriteName.value, editFavoriteName.value, user.id as number).then(res => {
                if (res.code == 200) {
                    editStats.value = false
                    beforRequestFavoriteNameAsCount(user.id as number)
                }
            })
        } else {
            window.$message.info("文件夹名不可以为空")
        }
    }

    function deleteFavorite(e: Event, favoriteName: string) {
        e.stopPropagation()
        e.preventDefault()
        deleteAsFavorite(favoriteName, user.id as number).then(res => {
            if (res.code == 200) {
                beforRequestFavoriteNameAsCount(user.id as number)
            }
        })
    }

    function reRequest(){
        findFiles(clickEventFavoriteName.value as string)
    }

    const router = useRouter()
    function tableEvent(data:RowType) {

        return{
            onClick(){
                 router.push({
                   path:`/reader/${favoriteToarticleMap.get(data.key as number)}`
                })
            }
        }
    }
</script>

<template>
    <div id="favorite">
        <n-modal v-model:show="stats">
            <n-card
                    style="width: 260px"
                    title="添加"
                    :bordered="false"
            >
                <template #header-extra>
                    <n-icon size="20" color="red" @click="stats=!stats">
                        <Close/>
                    </n-icon>
                </template>
                <n-input v-model:value="addFavoriteName" placeholder="输入收藏夹名"/>
                <template #footer>
                    <n-button type="success" @click="addCloseModal">
                        添加
                    </n-button>
                </template>
            </n-card>
        </n-modal>

        <n-modal v-model:show="editStats">
            <n-card
                    style="width: 260px"
                    title="修改"
                    :bordered="false"
            >
                <template #header-extra>
                    <n-icon size="20" color="red" @click="editStats=!editStats">
                        <Close/>
                    </n-icon>
                </template>
                <n-input v-model:value="editFavoriteName" placeholder="输入收藏夹名"/>
                <template #footer>
                    <n-button type="success" @click="editCloseModal">
                        修改
                    </n-button>
                </template>
            </n-card>
        </n-modal>


        <n-layout>
            <n-layout has-sider>
                <n-layout-sider>
                    <!--                    <n-input :disabled="favoriteList.length<=1" placeholder="收藏夹名">-->
                    <!--                        <template #prefix>-->
                    <!--                            <n-icon :component="FlashOutline"/>-->
                    <!--                        </template>-->
                    <!--                    </n-input>-->
                    <n-card :title="ele.favoriteName" v-for="ele in favoriteList" @click="findFiles(ele.favoriteName)"
                            hoverable>
                        <template v-if="ele.favoriteName == '默认收藏夹'" #header-extra>
                            <n-tooltip
                                    placement="right"
                                    trigger="hover"
                                    :style="{backgroundColor:'white',color:'#000',fontSize:'10px'}">
                                <template #trigger>
                                    <n-icon size="20" color="#0e7a0d" @click="stats=!stats">
                                        <AddCircleOutline/>
                                    </n-icon>
                                </template>
                                <span>添加新的收藏夹</span>
                            </n-tooltip>
                        </template>

                        <template v-if="ele.favoriteName != '默认收藏夹'" #header-extra>
                            {{ele.countAsName - 1}}
                        </template>
                        <n-space v-if="ele.favoriteName != '默认收藏夹'">
                            <n-icon size="20" @click="editFavorite($event,ele.favoriteName)" color="#2080f0">
                                <PricetagsOutline/>
                            </n-icon>
                            <n-icon size="20" @click="deleteFavorite($event,ele.favoriteName)" color="#d03050">
                                <Trash/>
                            </n-icon>
                        </n-space>
                    </n-card>
                </n-layout-sider>
                <n-layout-content>
                    <!--                    <n-input :disabled="!data.length" placeholder="文章名">-->
                    <!--                        <template #prefix>-->
                    <!--                            <n-icon :component="FlashOutline"/>-->
                    <!--                        </template>-->
                    <!--                    </n-input>-->
                    <n-data-table
                            :columns="columns"
                            :data="data"
                            :checked-row-keys="selectRow"
                            :max-height="tableHeight"
                            :min-height="tableHeight"
                            :row-key="rowKey"
                            :row-props="tableEvent"
                            @update:checked-row-keys="handleCheck"/>
                    <Pagination ref="paginationRef" @pageChange="reRequest"/>

                </n-layout-content>
            </n-layout>
        </n-layout>
    </div>
</template>

<style lang="scss" scoped>
    #favorite {
        padding: 10px 20px 0px 20px;

        .n-layout-sider {
            padding: 0;
            margin-right: 10px;
            height: calc(100vh - 10px);
            min-height: 600px;
        }

        .n-layout-content {
            padding: 0;
            height: calc(100vh - 10px);
            min-height: 600px;
            min-width: 600px;
            width: calc(100vw - 100px);

            .n-input {
                margin-bottom: 10px;
            }
        }

        /*.n-data-table {*/
        /*    height: calc(100vh - 20px);*/
        /*}*/

        .n-card {
            margin: 10px 0px;
            margin-top: 0px;
            background-image: url("/background_fa.png");
            background-size: 20%;
            background-position: 90% 150%;
            background-repeat: no-repeat;
        }

        .n-pagination {
            margin-top: 10px;
        }
    }
</style>
