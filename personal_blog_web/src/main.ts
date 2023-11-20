import { createApp } from 'vue'
import './style.css'
// 通用字体
import 'vfonts/Lato.css'
// 等宽字体
import 'vfonts/FiraCode.css'
//重置样式
import "normalize.css"
//动画样式
import "animate.css"
//md样式
import 'md-editor-v3/lib/style.css';
//字体样式
import './assets/font.css'
//请求进度条样式
import 'nprogress/nprogress.css'

//@ts-ignore
import  App from './App.vue'
import router from "./config/router.config"

// waring("111")


const app = createApp(App)
app.config.warnHandler = () => null
app.use(router)
app.mount('#app')
