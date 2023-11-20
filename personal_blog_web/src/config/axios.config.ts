import axios, {AxiosHeaders, AxiosRequestConfig} from 'axios';
// @ts-ignore
import router from "./router.config"
// @ts-ignore
import Progress from 'nprogress'
const baseURL: string = import.meta.env.BLOG_REQUEST_URL
const headerToken: string = import.meta.env.BLOG_REQUEST_HEADERS_TOKEN_NAME

const message = window.$message
Progress.configure({
    easing: 'speed',  // 动画方式
    speed: 500,  // 递增进度条的速度
    showSpinner: false, // 是否显示加载ico
    trickleSpeed: 200, // 自动递增间隔
    minimum: 0.3 // 初始化时的最小百分比
});


const axiosInstance = axios.create({
    baseURL,
    method: "POST",
    timeout: 15000,
    headers: {
        "Content-Type": "application/json",
    }
});

axiosInstance.interceptors.request.use(
    (config: any) => {
        Progress.start()
        return {
            ...config,
            headers: {
                ...config.headers,
                Authorization: localStorage.getItem(headerToken) ? localStorage.getItem(headerToken):""
            },
        };
    },
    (error) => {
        Progress.done()
        message.error("请求失败")
    },
);


axiosInstance.interceptors.response.use(
    function (response: any) {
        Progress.done()
        const dataApply: any = {
            code: response.data.code,
            data: undefined
        }
        if (response.data.code == 5006) {
            for (let ele of response.data.data) {
                message.error(ele)
            }
            return Promise.resolve(dataApply)
        } else if (response.data.code >= 5000) {
            message.error(response.data.data)
            localStorage.clear()
            router.push("/")
            return Promise.resolve(dataApply)
        } else if (response.data.code == 4005) {
            message.success(response.data.data)
            router.push("/index")
            return Promise.resolve(dataApply)
        } else if (typeof response.data.data == "string") {
            if (response.data.code == 200) {
                message.success(response.data.data)
            } else {
                message.error(response.data.data)
            }
            return Promise.resolve(response)
        }
        return Promise.resolve(response);
    },
    function (error) {
        Progress.done()
        message.error("操作失败")
        localStorage.clear()
        router.push("/")
    },
);

const request = async <T>(config: AxiosRequestConfig): Promise<any> => {
    let response = await  axiosInstance(config);
    return response.data
}
export default request;
