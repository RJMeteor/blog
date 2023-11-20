import {MenuOption} from "naive-ui";
import {renderIcon} from "../../../hook/handler/icon";
import {HeartCircle} from "@vicons/ionicons5";
import {h} from "vue";
import {RouterLink} from "vue-router";

const options: MenuOption[] = [
    {
        label: '关注',
        key: '1',
        icon: renderIcon(HeartCircle)
    },
    {
        label: () =>
            h(
                RouterLink,
                {
                    to: {
                        path: "/"
                    }
                },
                {default: () => '评论/点赞'}
            ),
        key: '2',
        icon: renderIcon(HeartCircle)
    },
    {
        label: '文章推送',
        key: '3',
        icon: renderIcon(HeartCircle)
    },
]

export {
    options as modalOptions
}