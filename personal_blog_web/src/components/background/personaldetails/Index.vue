<script setup lang="ts">
    import {defineComponent, ref, onMounted, watch} from "vue"
    import {ArchiveOutline} from '@vicons/ionicons5'
    import {SelectOption, UploadFileInfo} from "naive-ui"
    import {informationView, update} from "../../../hook/request/user.request";
    import {User, UserInfo} from "../../../define/user";

    const intro = ref<string>()
    const phone = ref<string>()
    const name = ref<string>()
    const art = ref<number>()
    const information = ref<UserInfo>()
    const pass = ref<string>()
    const sex = ref<number>()
    const artList = ref<Array<{
        label: string,
        value: number
    }>>([])

    onMounted(function () {
        informationView(JSON.parse(localStorage.getItem("user") as string).id).then(res => {
            information.value = res.data
            name.value = information.value.blogUser.userName
            phone.value = information.value.blogUser.userPhone
            intro.value = information.value.blogUser.userIntro
            art.value = information.value.blogUser.userArtId
            pass.value = information.value.blogUser.userPass
            sex.value = information.value.blogUser.userSex
            for (let blogArtListElement of res.data.blogArtList) {
                artList.value.push({
                    label: blogArtListElement.artName,
                    value: blogArtListElement.id
                })
            }
        })
    })

    function handleChange(value: Event) {
        console.log(value.target?.defaultValue)
        //@ts-ignore
        sex.value = +value.target?.defaultValue
    }

    function getUserId(): number {
        const user = JSON.parse(localStorage.getItem("user") as string) as User
        return user.id as number
    }

    function editPersonalInfo() {
        const user: User = {
            id: information.value?.blogUser.id,
            userName: name.value,
            userArtId: art.value,
            userIntro: intro.value,
            userPhone: phone.value,
            userPass: pass.value,
            userSex: sex.value
        }
        update(user)
    }

    function handleFinish({
                              file,
                              event
                          }: {
        file: UploadFileInfo
        event?: ProgressEvent
    }) {
        let response = JSON.parse((event?.target as XMLHttpRequest).response);
        let user = JSON.parse(localStorage.getItem("user") as string) as User
        if (response.code == 200){
            window.$message.success("上传成功")
        }
        if (response.data instanceof Array) {
            for (const ele of response.data) {
                user.userImage = ele as string
                (information.value?.blogUser as User).userImage = ele as string
            }
        }
    }

    const headerToken: string = import.meta.env.BLOG_REQUEST_HEADERS_TOKEN_NAME
    const uploadUrl = import.meta.env.BLOG_REQUEST_URL + 'blogUser/uploadImg/'
    const token = localStorage.getItem(headerToken) ? localStorage.getItem(headerToken) : ""

</script>

<template>
    <div id="personaldetails">
        <n-page-header :subtitle="information?.blogUser?.userIntro" @back="handleBack">
            <n-grid :cols="6">
                <n-gi>
                    <n-statistic label="关注" :value="information?.attention"/>
                </n-gi>
                <n-gi>
                    <n-statistic label="粉丝" :value="information?.fans"/>
                </n-gi>
                <n-gi>
                    <n-statistic label="浏览" :value="information?.browse"/>
                </n-gi>
                <n-gi>
                    <n-statistic label="文章" :value="information?.article"/>
                </n-gi>
                <n-gi>
                    <n-statistic label="收藏" :value="information?.favorite"/>
                </n-gi>
                <n-gi>
                    <n-statistic label="被赞" :value="information?.praise"/>
                </n-gi>
            </n-grid>
            <template #title>
                {{information?.blogUser.userName}}
            </template>
            <template #avatar>
                <n-avatar
                        :src="information?.blogUser?.userImage?information?.blogUser?.userImage:'/icon.png'"
                />
            </template>
        </n-page-header>
        <n-layout has-sider>
            <n-layout-sider content-style="padding: 24px;">
                <n-upload
                        multiple
                        directory-dnd
                        :headers="{
      'Authorization': token
    }"
                        :data="{
                    'userId':getUserId()
                        }"
                        :action="uploadUrl"
                        @finish="handleFinish"
                        :max="1"
                >
                    <n-upload-dragger>
                        <div style="margin-bottom: 12px">
                            <n-icon size="48" :depth="3">
                                <ArchiveOutline/>
                            </n-icon>
                        </div>
                        <n-text>
                            点击或者拖动文件到该区域来上传头像
                        </n-text>
                    </n-upload-dragger>
                </n-upload>
            </n-layout-sider>
            <n-layout-content content-style="padding: 24px;">
                <n-form ref="formRef">
                    <n-form-item path="name" label="姓名">
                        <n-input
                                v-model:value="name"
                                type="text"/>
                    </n-form-item>
                    <n-form-item path="sex" label="性别">
                        <n-space>
                            <n-radio
                                    :checked="sex === 0"
                                    value="0"
                                    name="basic-demo"
                                    @change="handleChange"
                            >
                               女
                            </n-radio>
                            <n-radio
                                    :checked="sex === 1"
                                    value="1"
                                    name="basic"
                                    @change="handleChange"
                            >
                                男
                            </n-radio>
                        </n-space>
                    </n-form-item>
                    <n-form-item path="local" label="技术领域">
                        <n-select
                                placeholder="选择技术领域"
                                :options="artList"
                                v-model:value="art"
                        />
                    </n-form-item>
                    <n-form-item path="phone" label="电话号码">
                        <n-input
                                type="text"
                                v-model:value="phone"
                        />
                    </n-form-item>
                    <n-form-item path="pass" label="密码">
                        <n-input
                                type="text"
                                v-model:value="pass"
                        />
                    </n-form-item>
                    <n-form-item
                            first
                            path="reenteredPassword"
                            label="个人简介"
                    >
                        <n-input
                                type="textarea"
                                placeholder="个人简介"
                                v-model:value="intro"
                                round clearable/>
                    </n-form-item>
                    <n-row :gutter="[0, 24]">
                        <n-col :span="24">
                            <div style="display: flex; justify-content: flex-end">
                                <n-button
                                        strong @click="editPersonalInfo" secondary type="success"
                                >
                                    修改
                                </n-button>
                            </div>
                        </n-col>
                    </n-row>
                </n-form>
            </n-layout-content>
        </n-layout>
    </div>
</template>

<style lang="scss" scoped>
    #personaldetails {
        padding: 10px;

        .uploadavd {
            margin-top: 10px;
            width: 300px;

        }
    }
</style>
