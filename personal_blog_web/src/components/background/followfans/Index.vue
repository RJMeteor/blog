<script setup lang="ts">
    import {FlashOutline} from '@vicons/ionicons5'
    import {onMounted, ref} from "vue"
    import {Fans} from "../../../define/fans";
    import {Attention} from "../../../define/attention";
    import {attentionAsList, fansAsList} from "../../../hook/request/fansAndAttention.request";
    import {User} from "../../../define/user";

    const fansList = ref<Array<Fans>>([])
    const attentionList = ref<Array<Attention>>([])
    onMounted(function () {
      const user:User =  JSON.parse(localStorage.getItem("user") as string)
        fansAsList(user.id).then(res=>{
            fansList.value = res.data
        })
        attentionAsList(user.id).then(res=>{
            attentionList.value = res.data
        })
    })
</script>

<template>
    <div id="followfans">
        <n-scrollbar>
            <n-tabs type="segment">
                <n-tab-pane name="chap1" tab="关注">
<!--                    <n-input :disabled="!fansList.length" placeholder="关注名">-->
<!--                        <template #prefix>-->
<!--                            <n-icon :component="FlashOutline"/>-->
<!--                        </template>-->
<!--                    </n-input>-->
                    <n-list v-for="ele in attentionList">
                        <n-list-item>
                            <template #prefix>
                                <n-avatar
                                        round
                                        :size="50"
                                        :src="ele.attentionUser?.userImage?ele.attentionUser?.userImage:'/icon.png'"
                                />
                            </template>
                            <template #suffix>
<!--                                <n-button type="error" @click="deleteAtt(ele.)">取消关注</n-button>-->
                            </template>
                            <n-thing
                                    :title="ele.attentionUser?.userName"
                                    :description="ele.attentionUser?.userIntro">
                            </n-thing>
                        </n-list-item>
                    </n-list>
                </n-tab-pane>
                <n-tab-pane name="chap2" tab="粉丝">
<!--                    <n-input :disabled="!fansList.length" placeholder="粉丝名">-->
<!--                        <template #prefix>-->
<!--                            <n-icon :component="FlashOutline"/>-->
<!--                        </template>-->
<!--                    </n-input>-->
                    <n-list v-for="ele in fansList">
                        <n-list-item>
                            <template #prefix>
                                <n-avatar
                                        round
                                        :size="50"
                                        :src="ele.fansUser?.userImage?ele.fansUser?.userImage:'/icon.png'"
                                />
                            </template>
                            <template #suffix>
<!--                                <n-button type="error">删除</n-button>-->
                            </template>
                            <n-thing
                                    :title="ele.fansUser?.userName"
                                    :description="ele.fansUser?.userIntro">
                            </n-thing>
                        </n-list-item>
                    </n-list>
                </n-tab-pane>
            </n-tabs>
        </n-scrollbar>
    </div>
</template>

<style lang="scss" scoped>
    #followfans {
        height: calc(100vh - 20px);
        padding: 10px;
    }
    .n-avatar {
        cursor: pointer;
        background-color: white !important;
    }
</style>
