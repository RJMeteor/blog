import {DropdownOption, MenuOption} from "naive-ui";
import {h} from "vue";
import {RouterLink} from "vue-router";


const aoptions: MenuOption[]  = [
    {
        label: () =>
            h(
                RouterLink,
                {
                    to: {
                        path: "/background/personaldetails"
                    }
                },
                {default: () => '个人信息'}
            ),
        key: 'personaldetails',
    },
    {
        label: () =>
            h(
                RouterLink,
                {
                    to: {
                        path: "/background/followfans"
                    }
                },
                {default: () => '关注/粉丝'}
            ),
        key: 'followfans',
    },
    // {
    //     label: () =>
    //         h(
    //             RouterLink,
    //             {
    //                 to: {
    //                     path: "/background/dataanalysis"
    //                 }
    //             },
    //             {default: () => '数据分析'}
    //         ),
    //     key: 'dataanalysis',
    // },
]

function renderDropdownLabel(option: DropdownOption) {
    if (option.name) {
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
    renderDropdownLabel as siderRender,
    aoptions as siderMenu
}