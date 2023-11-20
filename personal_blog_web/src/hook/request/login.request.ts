import request from '../../config/axios.config'
import {Login, Regist} from "../../define/login.request";

//登录
function loginRequest(data:Login):Promise<any>{
    return  request({
        method:"POST",
        url:'crux/login',
        data
    })
}

//退出登录
function logoutRequest():Promise<any>{
    return  request({
        method:"get",
        url:'crux/logout'
    })
}

//注册
function registRequest(data:Regist):Promise<any>{
    return  request({
        method:"POST",
        url:'crux/regist',
        data
    })
}

export {
    loginRequest,
    registRequest,
    logoutRequest
}