<script setup lang="ts">
    import {ref, reactive,nextTick} from "vue"
    import {loginRequest, registRequest} from '../hook/request/login.request'
    import {Login, Regist} from "../define/login.request";
    import {Sse} from "../hook/correspondence/sse";
    import QrcodeVue from 'qrcode.vue'
    import {useRouter} from "vue-router";
    const isLogin = ref<boolean>(false)
    const loginForm = reactive<Login>({
        phone: '',
        password: '',
    })
    const registerForm = reactive<Regist>({
        phone: '',
        password: '',
    })

    let router = useRouter();
    function login() {
        loginRequest(loginForm).then(res => {
            localStorage.setItem("user", JSON.stringify(res.data))
            if (res.code == 200) {
                localStorage.setItem(import.meta.env.BLOG_REQUEST_HEADERS_TOKEN_NAME,res.data.token)
                new Sse()
                window.$message.success("登录成功")
                router.push({path:"/index"})
            }
        })
    }

    function regist() {
        registRequest(registerForm)
    }

    /*
    * 二维码生成
    * */
    const qrData = ref<string>("http://47.108.189.140")  //二维码内容
    const qrSize = ref<number>(0)
    nextTick(function () {
        qrSize.value = parseFloat(getComputedStyle(document.documentElement).fontSize)*15
    })
    const qrOptions = reactive({ // 二维码的配置选项
        size: 150, // 二维码的尺寸
        color: '#000000', // 二维码的颜色
        background: '#ffffff', // 二维码的背景色
    })

    const formStatus = ref<boolean>(true)
    const qrCodeStatus = ref<boolean>(false)
    const phoneStatus = ref<boolean>(false)

    function changCode() {
        formStatus.value = true;
        qrCodeStatus.value = false
    }
    function changePhoneStatus() {
        phoneStatus.value = false;
        formStatus.value = true
    }
</script>
<template>
    <div id="login">
        <div class="circular  smal"></div>
        <div class="circular  large"></div>
        <div v-if="formStatus" class="loginBox">
            <p class="login_title">登录</p>
            <div class="tripartiteLogin">
                <div @click="qrCodeStatus = true" class="login_entrance">
                    <div class="qq">
                        <svg t="1697614869807" class="icon" viewBox="0 0 1024 1024" version="1.1"
                             xmlns="http://www.w3.org/2000/svg" p-id="1848" width="200" height="200">
                            <path d="M511.09761 957.257c-80.159 0-153.737-25.019-201.11-62.386-24.057 6.702-54.831 17.489-74.252 30.864-16.617 11.439-14.546 23.106-11.55 27.816 13.15 20.689 225.583 13.211 286.912 6.767v-3.061z"
                                  fill="#FAAD08" p-id="1849"></path>
                            <path d="M496.65061 957.257c80.157 0 153.737-25.019 201.11-62.386 24.057 6.702 54.83 17.489 74.253 30.864 16.616 11.439 14.543 23.106 11.55 27.816-13.15 20.689-225.584 13.211-286.914 6.767v-3.061z"
                                  fill="#FAAD08" p-id="1850"></path>
                            <path d="M497.12861 474.524c131.934-0.876 237.669-25.783 273.497-35.34 8.541-2.28 13.11-6.364 13.11-6.364 0.03-1.172 0.542-20.952 0.542-31.155C784.27761 229.833 701.12561 57.173 496.64061 57.162 292.15661 57.173 209.00061 229.832 209.00061 401.665c0 10.203 0.516 29.983 0.547 31.155 0 0 3.717 3.821 10.529 5.67 33.078 8.98 140.803 35.139 276.08 36.034h0.972z"
                                  fill="#000000" p-id="1851"></path>
                            <path d="M860.28261 619.782c-8.12-26.086-19.204-56.506-30.427-85.72 0 0-6.456-0.795-9.718 0.148-100.71 29.205-222.773 47.818-315.792 46.695h-0.962C410.88561 582.017 289.65061 563.617 189.27961 534.698 185.44461 533.595 177.87261 534.063 177.87261 534.063 166.64961 563.276 155.56661 593.696 147.44761 619.782 108.72961 744.168 121.27261 795.644 130.82461 796.798c20.496 2.474 79.78-93.637 79.78-93.637 0 97.66 88.324 247.617 290.576 248.996a718.01 718.01 0 0 1 5.367 0C708.80161 950.778 797.12261 800.822 797.12261 703.162c0 0 59.284 96.111 79.783 93.637 9.55-1.154 22.093-52.63-16.623-177.017"
                                  fill="#000000" p-id="1852"></path>
                            <path d="M434.38261 316.917c-27.9 1.24-51.745-30.106-53.24-69.956-1.518-39.877 19.858-73.207 47.764-74.454 27.875-1.224 51.703 30.109 53.218 69.974 1.527 39.877-19.853 73.2-47.742 74.436m206.67-69.956c-1.494 39.85-25.34 71.194-53.24 69.956-27.888-1.238-49.269-34.559-47.742-74.435 1.513-39.868 25.341-71.201 53.216-69.974 27.909 1.247 49.285 34.576 47.767 74.453"
                                  fill="#FFFFFF" p-id="1853"></path>
                            <path d="M683.94261 368.627c-7.323-17.609-81.062-37.227-172.353-37.227h-0.98c-91.29 0-165.031 19.618-172.352 37.227a6.244 6.244 0 0 0-0.535 2.505c0 1.269 0.393 2.414 1.006 3.386 6.168 9.765 88.054 58.018 171.882 58.018h0.98c83.827 0 165.71-48.25 171.881-58.016a6.352 6.352 0 0 0 1.002-3.395c0-0.897-0.2-1.736-0.531-2.498"
                                  fill="#FAAD08" p-id="1854"></path>
                            <path d="M467.63161 256.377c1.26 15.886-7.377 30-19.266 31.542-11.907 1.544-22.569-10.083-23.836-25.978-1.243-15.895 7.381-30.008 19.25-31.538 11.927-1.549 22.607 10.088 23.852 25.974m73.097 7.935c2.533-4.118 19.827-25.77 55.62-17.886 9.401 2.07 13.75 5.116 14.668 6.316 1.355 1.77 1.726 4.29 0.352 7.684-2.722 6.725-8.338 6.542-11.454 5.226-2.01-0.85-26.94-15.889-49.905 6.553-1.579 1.545-4.405 2.074-7.085 0.242-2.678-1.834-3.786-5.553-2.196-8.135"
                                  fill="#000000" p-id="1855"></path>
                            <path d="M504.33261 584.495h-0.967c-63.568 0.752-140.646-7.504-215.286-21.92-6.391 36.262-10.25 81.838-6.936 136.196 8.37 137.384 91.62 223.736 220.118 224.996H506.48461c128.498-1.26 211.748-87.612 220.12-224.996 3.314-54.362-0.547-99.938-6.94-136.203-74.654 14.423-151.745 22.684-215.332 21.927"
                                  fill="#FFFFFF" p-id="1856"></path>
                            <path d="M323.27461 577.016v137.468s64.957 12.705 130.031 3.91V591.59c-41.225-2.262-85.688-7.304-130.031-14.574"
                                  fill="#EB1C26" p-id="1857"></path>
                            <path d="M788.09761 432.536s-121.98 40.387-283.743 41.539h-0.962c-161.497-1.147-283.328-41.401-283.744-41.539l-40.854 106.952c102.186 32.31 228.837 53.135 324.598 51.926l0.96-0.002c95.768 1.216 222.4-19.61 324.6-51.924l-40.855-106.952z"
                                  fill="#EB1C26" p-id="1858"></path>
                        </svg>
                        <span>QQ</span>
                    </div>
                </div>
                <div @click="phoneStatus = !phoneStatus" class="login_entrance">
                    <div class="email">
                        <svg t="1697615480511" class="icon" viewBox="0 0 1024 1024" version="1.1"
                             xmlns="http://www.w3.org/2000/svg" p-id="6842" width="200" height="200">
                            <path d="M512 512m-460.8 0a460.8 460.8 0 1 0 921.6 0 460.8 460.8 0 1 0-921.6 0Z"
                                  fill="#5FED70" p-id="6843"></path>
                            <path d="M544.256 255.488c4.096-17.408-6.656-34.816-24.064-38.912-17.408-4.096-34.816 6.656-38.912 24.064-4.096 17.408 7.68 62.976 17.92 65.024s40.96-32.768 45.056-50.176zM597.504 310.272c-5.12-16.896-23.552-26.624-40.448-21.504-16.896 5.12-50.688 37.888-47.616 48.128 3.072 10.24 49.152 18.944 66.048 13.824 17.408-5.12 27.136-23.04 22.016-40.448zM652.8 607.744c-44.544-0.512-82.432 28.672-95.232 69.12-1.024 3.584-5.632 5.12-8.704 2.56-45.056-35.84-66.048-61.44-98.304-100.352-9.728-11.264-35.328-45.568-59.392-90.112-2.048-3.584 0.512-7.68 4.096-8.192 47.616-7.168 84.48-48.128 84.48-97.792 0-54.784-45.568-99.84-100.352-98.816-44.544 0.512-79.872 30.72-93.696 70.656-17.408 51.2-29.696 147.968 71.68 293.888 64 92.16 214.528 181.76 314.88 155.136 43.008-11.264 76.8-47.104 78.848-92.672 2.048-55.808-42.496-102.912-98.304-103.424z"
                                  fill="#FFFFFF" p-id="6844"></path>
                        </svg>
                        <span>
                            电话号码</span>
                    </div>
                </div>
            </div>
            <p class="or">or</p>
            <div class="loginForm">
                <div class="phon">
                    <p class="label">电话号码</p>
                    <input class="input" v-model="loginForm.phone" type="text" placeholder="请填写电话号码...">
                </div>
                <div class="password">
                    <p class="label">密码</p>
                    <input class="input" v-model="loginForm.password" type="password" placeholder="请填写密码...">
                </div>
                <div class="submit">
                    <div class="input" @click="login">登录</div>
                </div>
                <p class="no_account">没有账号？<span @click="formStatus = !formStatus">注册</span></p>
            </div>
        </div>
        <div class="qr_code" v-if="qrCodeStatus">
            <QrcodeVue :value="qrData" :level="'M'" :render-as="'svg'" :size="qrSize"/>
            <p class="no_account"><span @click="changCode">账号登录</span></p>
        </div>
        <div class="registe" v-if="!formStatus">
            <p class="login_title">注册</p>
            <div class="loginForm">
                <div class="phon">
                    <p class="label">电话号码</p>
                    <input class="input" type="text" placeholder="请填写电话号码...">
                    <button class="code">获取验证码</button>
                </div>
                <div class="info_code">
                    <p class="label">验证码</p>
                    <input class="input" type="password" placeholder="请填写验证码...">
                </div>
                <div class="password">
                    <p class="label">密码</p>
                    <input class="input" type="password" placeholder="请填写密码...">
                </div>
                <div class="submit">
                    <div class="input">登录</div>
                </div>
                <p class="no_account">有账号？<span @click="formStatus = !formStatus">登录</span></p>
            </div>
        </div>
        <div class="registe" v-if="phoneStatus">
            <p class="login_title">电话号码登录</p>
            <div class="loginForm">
                <div class="phon">
                    <p class="label">电话号码</p>
                    <input class="input" type="text" placeholder="请填写电话号码...">
                    <button class="code">获取验证码</button>
                </div>
                <div class="info_code">
                    <p class="label">验证码</p>
                    <input class="input" type="password" placeholder="请填写验证码...">
                </div>
                <div class="submit">
                    <div class="input">登录</div>
                </div>
                <p class="no_account">记得密码？<span @click="changePhoneStatus">登录</span></p>
            </div>
        </div>
    </div>
</template>


<style lang="scss" scoped>
    #login {
        position: relative;
        width: 100vw;
        height: 100vh;
        flex-shrink: 0;
        border: 1px solid #979797;
        background: $blog_main_background;
        display: flex;

        .circular {
            opacity: 0.034;
            background: $blog_ex_background;
            flex-shrink: 0;
            position: absolute;

            &.smal {
                width: 21.375rem;
                height: 21.375rem;
                border-radius: 21.375rem;
                top: calc(50% - 21.375rem / 2 - 21.375rem / 2);
                left: calc(50% - 21.375rem / 2 - 21.375rem / 2);

            }

            &.large {
                width: 29.75rem;
                height: 29.75rem;
                border-radius: 29.75rem;
                top: calc(50% - 29.75rem / 2 + 29.75rem / 6);
                left: calc(50% - 29.75rem / 2 + 29.75rem / 4);
            }
        }

        .loginBox, .registe {
            width: 26.5625rem;
            height: 34.3125rem;
            border-radius: 0.3125rem;
            background: $blog_card_background;
            position: absolute;
            box-shadow: 0px 24px 20px -17px rgba(42, 35, 50, 0.78);
            top: calc(50% - 26.5625rem / 2 - 65px);
            left: calc(50% - 26.5625rem / 2);

            .login_title {
                color: #FFF;
                text-align: center;
                font-family: Montserrat;
                font-size: 1.25rem;
                font-style: normal;
                font-weight: 600;
                line-height: normal;
            }


            .tripartiteLogin {
                display: flex;
                justify-content: space-evenly;
                align-items: center;

                .login_entrance {
                    cursor: pointer;
                    width: 10.875rem;
                    height: 3.625rem;
                    flex-shrink: 0;
                    border-radius: 0.1875rem;
                    background: $blog_item_background;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    color: $blog_defalut_font_color;
                    font-family: Montserrat;
                    font-size: 1rem;
                    font-style: normal;
                    font-weight: 600;
                    line-height: normal;

                    .qq {
                        display: flex;
                        justify-content: center;
                        align-items: center;
                    }

                    .email {
                        display: flex;
                        justify-content: center;
                        align-items: center;
                    }

                    .icon {
                        width: 1.5625rem;
                        height: 1.5625rem;
                        margin-right: 3px;
                    }
                }
            }

            .or {
                color: #FFF;
                text-align: center;
                font-family: Montserrat;
                font-size: 0.875rem;
                font-style: normal;
                font-weight: 500;
                line-height: normal;
            }

            .loginForm {
                width: 100%;
                display: flex;
                justify-content: center;
                flex-wrap: wrap;

                .input {
                    caret-color: $blog_button_background;
                    color: $blog_defalut_font_color;
                    width: 22.6875rem;
                    height: 3.625rem;
                    flex-shrink: 0;
                    border-radius: 0.1875rem;
                    border: 1px solid rgba(211, 203, 219, 0.07);
                    background: $blog_main_background;

                    &::placeholder {
                        color: $blog_placehodler_color;
                        font-family: Montserrat;
                        font-size: 1rem;
                        font-style: normal;
                        font-weight: 500;
                        line-height: normal;
                        opacity: 0.5;
                        padding-left: 10px;
                    }
                }

                .label {
                    color: #FFF;
                    font-family: Montserrat;
                    font-size: 1rem;
                    font-style: normal;
                    font-weight: 500;
                    line-height: normal;
                    opacity: 0.9;
                }

                .submit {
                    cursor: pointer;

                    .input {
                        width: 22.6875rem;
                        height: 3.625rem;
                        flex-shrink: 0;
                        border-radius: 0.1875rem;
                        background: $blog_button_background;
                        margin-top: 2rem;
                        color: #FFF;
                        font-family: Montserrat;
                        font-size: 1.125rem;
                        font-style: normal;
                        font-weight: 500;
                        line-height: normal;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                    }
                }

                .no_account {
                    color: #FFF;
                    text-align: center;
                    font-family: Montserrat;
                    font-size: 1rem;
                    font-style: normal;
                    font-weight: 500;
                    line-height: normal;

                    span {
                        color: $blog_button_background;
                        font-family: Montserrat;
                        font-size: 1rem;
                        font-style: normal;
                        font-weight: 500;
                        line-height: normal;
                    }
                }
            }
        }

        .qr_code {
            width: 26.5625rem;
            height: 34.3125rem;
            border-radius: 0.3125rem;
            position: absolute;
            background: $blog_card_background;
            box-shadow: 0px 24px 20px -17px rgba(42, 35, 50, 0.78);
            top: calc(50% - 26.5625rem / 2 - 65px);
            left: calc(50% - 26.5625rem / 2);
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;

            .no_account {
                color: #FFF;
                text-align: center;
                font-family: Montserrat;
                font-size: 1rem;
                font-style: normal;
                font-weight: 500;
                line-height: normal;

                span {
                    color: $blog_button_background;
                    font-family: Montserrat;
                    font-size: 1rem;
                    font-style: normal;
                    font-weight: 500;
                    line-height: normal;
                }
            }
        }

        .registe {
            .phon {
                .input {
                    width: 15rem;
                }

                .code {
                    width: 7rem;
                    height: 3.625rem;
                    color: #fff;
                    font-family: Montserrat;
                    /*font-size: 1rem;*/
                    font-style: normal;
                    font-weight: 500;
                    line-height: normal;
                    border: none;
                    border-radius: unset;
                    border-radius: 0.1875rem;
                    margin-left: .6875rem;
                    background:$blog_main_background;
                }
            }
        }
    }
</style>

