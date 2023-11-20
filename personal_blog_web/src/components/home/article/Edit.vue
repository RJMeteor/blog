<script setup lang="ts">
    import {ref, watch, reactive, onMounted} from "vue"
    import {MdEditor} from 'md-editor-v3';
    import {uploadImg, seveArticle, readArticle, articles, editArticle} from '../../../hook/request/creation.request'
    import {Article, Articles} from "../../../define/creation.request";
    import {useRoute} from "vue-router"
    import {User} from "../../../define/user";

    const creationComment = ref<string>()
    const creationName = ref<string>()
    const route = useRoute()
    const article = ref<Articles>({})
    const user:User = JSON.parse(localStorage.getItem("user") as string)
    onMounted(function () {
        readArticle(+route.params.id,user.id as number).then(res => {
            article.value!.blogArticleContentMapping = res.data.blogArticleContentMapping
            article.value!.id = res.data.blogArticleContentMapping?.article?.id
            article.value!.articleContentMappingId = res.data.blogArticleContentMapping?.article?.articleContentMappingId
            creationComment.value = res.data.blogArticleContentMapping?.mdContent
            creationName.value = res.data.articleTitle
        })
    })

    function creationSave(commentNotHtml: string, h: Promise<any>) {
        h.then((html: string) => {
            let domParser = new DOMParser();
            let document1 = domParser.parseFromString(html, "text/html");
            article.value!.blogArticleContentMapping!.articleContentNotHtml = document1.body.textContent?.replaceAll("\n", "").replaceAll(/[" "]*/g, "")
            article.value!.blogArticleContentMapping!.articleContent = html;
            article.value!.blogArticleContentMapping!.mdContent = commentNotHtml;
            article.value!.articleTitle = creationName.value
            editArticle(article.value)
        })
    }

    function creationUploadImg(files: FileList, callback: Function) {
        const formData = new FormData();
        for (let file of Array.from(files)) {
            formData.append("file", file)
        }
        uploadImg(formData)
    }
</script>

<template>
    <n-input v-model:value="creationName" placeholder="标题"/>
    <MdEditor
            placeholder="请书写要分享的内容....."
            toolbarsExclude="['link', 'mermaid', 'katex', 'github']"
            v-model="creationComment"
            @onUploadImg="creationUploadImg"
            @onSave="creationSave"
            previewOnly
    >
    </MdEditor>

</template>

<style lang="scss" scoped>
    .md-editor {
        height: calc(100vh - 34px);
        width: 100vw;
    }
    .n-form-item .n-form-item-feedback-wrapper {
        min-height: 0;
    }
</style>
