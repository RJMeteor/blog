import {MenuOption, NBadge} from "naive-ui";
import {renderIcon} from "../../../hook/handler/icon";
import {HeartCircle} from "@vicons/ionicons5";
import {h, ref} from "vue";
import {RouterLink} from "vue-router";
import {User} from "../../../define/user";
import {messageAsStatus} from "../../../hook/request/user.request";


const options = ref<MenuOption[]>()

function ger(map: { "attention"?: boolean, "like"?: boolean, "privateletter"?: boolean, "comment"?: boolean, "articlepush"?: boolean }): MenuOption[] {

    return map && [
        {
            label: () =>
                h(
                    RouterLink,
                    {
                        to: {
                            path: "/index/privateletter"
                        }
                    },
                    {default: () => '私信'}
                ),
            key: 'privateletter',
            icon: map["privateletter"] ? () => h(NBadge, {dot: true}, renderIcon(HeartCircle)) : renderIcon(HeartCircle)
        },
        {
            label: () =>
                h(
                    RouterLink,
                    {
                        to: {
                            path: "/index/attention"
                        }
                    },
                    {default: () => '关注'}
                ),
            key: 'attention',
            icon: map["attention"] ? () => h(NBadge, {dot: true}, renderIcon(HeartCircle)) : renderIcon(HeartCircle)
        },
        {
            label: () =>
                h(
                    RouterLink,
                    {
                        to: {
                            path: "/index/comment"
                        }
                    },
                    {default: () => '评论'}
                ),
            key: 'comment',
            icon: map["comment"] ? () => h(NBadge, {dot: true}, renderIcon(HeartCircle)) : renderIcon(HeartCircle)
        },
        {
            label: () =>
                h(
                    RouterLink,
                    {
                        to: {
                            path: "/index/like"
                        }
                    },
                    {default: () => '点赞'}
                ),
            key: 'like',
            icon: map["like"] ? () => h(NBadge, {dot: true}, renderIcon(HeartCircle)) : renderIcon(HeartCircle)
        },
        {
            label: () =>
                h(
                    RouterLink,
                    {
                        to: {
                            path: "/index/articlepush"
                        }
                    },
                    {default: () => '文章推送'}
                ),
            key: 'articlepush',
            icon: map["articlepush"] ? () => h(NBadge, {dot: true}, renderIcon(HeartCircle)) : renderIcon(HeartCircle)
        },
    ]
}

function messageStatusView() {
    const user: User = JSON.parse(localStorage.getItem("user") as string)
    if (user.id) {
        messageAsStatus(user.id).then(res => {
            options.value = ger(res.data)
        }).catch(
            reason => {
                options.value = ger({})
            }
        )
    }
}


export {
    options as modalOptions,
    messageStatusView,
    ger
}