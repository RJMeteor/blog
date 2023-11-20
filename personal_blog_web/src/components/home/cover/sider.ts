import {DropdownOption, MenuOption} from "naive-ui";
import {h} from "vue";
import {RouterLink} from "vue-router";
import {renderIcon} from "../../../hook/handler/icon";
import {RoseSharp, SparklesSharp, PawSharp} from "@vicons/ionicons5"
import {User} from "../../../define/user";
import router from "../../../config/router.config";

const options = [
    {
        label: '个人主页',
        name:"/homepage",
        key: 'homepage'
    },
    {
        label: '后台数据',
        name:"/background",
        key: 'background'
    },
    {
        label: '退出登录',
        key: 'loginout'
    },
]

const aoptions: MenuOption[]  = [
    {
        label: () =>
            h(
                RouterLink,
                {
                    to: {
                        path: "/index/article"
                    }
                },
                {default: () => '文章'}
            ),
        key: 'article',
        icon:renderIcon(RoseSharp)
    },
    {
        label: () =>
            h(
                RouterLink,
                {
                    to: {
                        path: "/index/creation"
                    }
                },
                {default: () => '创作'}
            ),
        key: 'creation',
        icon:renderIcon(PawSharp)
    },
    {
        label: () =>
            h(
                RouterLink,
                {
                    to: {
                        path: "/index/favorite"
                    }
                },
                {default: () => '收藏'}
            ),
        key: 'favorite',
        icon:renderIcon(SparklesSharp)
    },
]

function renderDropdownLabel(option: DropdownOption) {
    const  user:User = JSON.parse(localStorage.getItem("user") as string)
    if (option.name) {
        if (option.name == "/homepage"){
            return  h(
                //@ts-ignore
                RouterLink,
                {
                    to: {
                        path: `${option.name}/${user.id}`
                    }
                },
                {
                    default: () => option.label
                }
            )
        }
        return  h(
            //@ts-ignore
            RouterLink,
            {
                to: {
                    path: option.name
                }
            },
            {
                default: () => option.label
            }
        )
    }

    return h(
        "div",
        null,
        {default:() => option.label}
    )
}

export {
    options as headerOptions,
    renderDropdownLabel as siderRender,
    aoptions as siderMenu
}