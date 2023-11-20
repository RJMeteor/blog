<script setup lang="ts">
    import {ref, watch, reactive} from "vue"
    import {MdEditor} from 'md-editor-v3';
    import {uploadImg, seveArticle} from '../../../hook/request/creation.request'
    import {Article} from "../../../define/creation.request";

    const creationComment = ref<string>()
    const creationName = ref<string>()

    function creationSave(commentNotHtml: string, h: Promise<any>) {
        const article: Article = {};
        h.then((html: string) => {
            let domParser = new DOMParser();
            let document1 = domParser.parseFromString(html, "text/html");
            article.articleContentNotHtml = document1.body.textContent?.replaceAll("\n", "").replaceAll(/[" "]*/g, "")
            article.articleContent = html;
            article.mdContent = commentNotHtml;
            article.articleTitle = creationName.value
            article.userId = JSON.parse(localStorage.getItem("user") as string).id
            seveArticle(article)
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
        height: calc(100% - 34px);
    }

    .n-form-item .n-form-item-feedback-wrapper {
        min-height: 0;
    }
</style>
