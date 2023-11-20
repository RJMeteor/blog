import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite'
import { NaiveUiResolver  } from 'unplugin-vue-components/resolvers'

import { resolve } from 'path';

export default defineConfig({
    plugins: [
        vue(),
        Components({
            resolvers: [NaiveUiResolver ()],
        }),
    ],
    resolve:{
        alias:[
            {
                find:"@",
                replacement:resolve(__dirname, 'src')
            }
        ]
    },
    css: {
        preprocessorOptions: {
            scss: {
                // 配置全局sass变量注入
                additionalData: '@import "@/define/styles/variable.scss";'
            }
        }
    },
    // 以 envPrefix 开头的环境变量会通过 import.meta.env 暴露在你的客户端源码中。
    envPrefix:"BLOG_",
    server:{
        port:9000,
    },
})
