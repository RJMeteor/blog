import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import {MessageType} from "../define/message";

const routes: Array<RouteRecordRaw> = [
    {
        path: "/",
        component: () => import("../components/Login-a.vue" as string),
        meta: {
            explain: "登录",
            name: "login"
        }
    },
    {
        path: "/edit/:id",
        component: () => import("../components/home/article/Edit.vue" as string),
        meta: {
            explain: "编辑",
            name: "edit"
        }
    },
    {
        path: "/reader/:id",
        component: () => import("../components/home/article/Reader.vue" as string),
        meta: {
            explain: "阅读",
            name: "reader"
        }
    },
    {
        path: "/empty",
        component: () => import("../define/components/Empty.vue" as string),
        meta: {
            explain: "notfound",
            name: "empty"
        }
    },
    {
        path: "/index",
        redirect: "/index/article",
        components: {
            //@ts-ignore
            default: () => import("../components/home/Index.vue")
        },
        meta: {
            explain: "首页",
            name: "index"
        },
        children: [
            {
                path: "article",
                //@ts-ignore
                components: {
                    //@ts-ignore
                    default: () => import("../components/home/article/Index.vue")
                },
                meta: {
                    explain: "文章",
                    name: "article"
                },
            },
            {
                path: "creation",
                //@ts-ignore
                components: {
                    //@ts-ignore
                    default: () => import("../components/home/creation/Index.vue")
                },
                meta: {
                    explain: "创作",
                    name: "creation"
                },
            },
            {
                path: "favorite",
                //@ts-ignore
                components: {
                    //@ts-ignore
                    default: () => import("../components/home/favorite/Index.vue")
                },
                meta: {
                    explain: "收藏",
                    name: "favorite"
                },
            },
            {
                path: "articlepush",
                //@ts-ignore
                components: {
                    //@ts-ignore
                    message: () => import("../components/home/messagecenter/articlepush/Index.vue")
                },
                meta: {
                    explain: "文章推送",
                    name: "articlepush"
                },
            },
            {
                path: "attention",
                //@ts-ignore
                components: {
                    //@ts-ignore
                    message: () => import("../components/home/messagecenter/attention/Index.vue")
                },
                meta: {
                    explain: "关注",
                    name: "attention"
                },
            },
            {
                path: "comment",
                //@ts-ignore
                components: {
                    //@ts-ignore
                    message: () => import("../components/home/messagecenter/comment/Index.vue")
                },
                meta: {
                    explain: "评论",
                    name: "comment"
                },
            },
            {
                path: "like",
                //@ts-ignore
                components: {
                    //@ts-ignore
                    message: () => import("../components/home/messagecenter/like/Index.vue")
                },
                meta: {
                    explain: "点赞",
                    name: "like"
                },
            },
            {
                path: "privateletter",
                //@ts-ignore
                components: {
                    //@ts-ignore
                    message: () => import("../components/home/messagecenter/privateletter/Index.vue")
                },
                meta: {
                    explain: "私信",
                    name: "privateletter"
                },
            }
        ]
    },
    {
        path: "/homepage/:id",
        //@ts-ignore
        component: () => import("../components/homepage/Index.vue"),
        meta: {
            explain: "个人主页",
            name: "homepage"
        }
    },
    {
        path: "/background",
        redirect: "/background/personaldetails",
        //@ts-ignore
        component: () => import("../components/background/Index.vue"),
        meta: {
            explain: "后台数据",
            name: "background"
        },
        children: [
            {
                path: "dataanalysis",
                //@ts-ignore
                component: () => import("../components/background/dataanalysis/Index.vue"),
                meta: {
                    explain: "数据分析",
                    name: "dataanalysis"
                },
            },
            {
                path: "followfans",
                //@ts-ignore
                component: () => import("../components/background/followfans/Index.vue"),
                meta: {
                    explain: "关注/粉丝",
                    name: "followfans"
                },
            },
            {
                path: "personaldetails",
                //@ts-ignore
                component: () => import("../components/background/personaldetails/Index.vue"),
                meta: {
                    explain: "个人信息",
                    name: "personaldetails"
                },
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

const exclude = ["privateletter", "comment","like", "attention", "articlepush"]
const broadcastChannel = new BroadcastChannel(`message`)
router.beforeResolve((to, from, next) => {
    if (to.matched.length === 0) {
        next({
            path: "/empty"
        })
    }
    const arr = to.path.split("/");
    let message: MessageType = {
        modalState: false,
        activeMenu: to.meta?.name as string
    }
    if (!exclude.includes(arr[arr.length - 1])) {
        if (to.path.indexOf("homepage") < 0 && to.path.indexOf("reader") < 0) {
            localStorage.setItem("link", JSON.stringify(to.path))
        }
    } else if (exclude.includes(arr[arr.length - 1])) {
        message = {
            modalState: true,
            activeMenu: to.meta?.name as string
        }
    }
    broadcastChannel.postMessage(message)
    localStorage.setItem("message", JSON.stringify(message))
    localStorage.setItem("activeMenu", JSON.stringify(to.meta?.name))
    next()
})

export default router
