import request from '../../config/axios.config'
import {Login, Regist} from "../../define/login.request";
import {User, UserInfo} from "../../define/user";
import {R} from "../../define/r";

//用户全部信息
function informationView(userId:number):Promise<R<UserInfo>>{
    return  request({
        method:"get",
        url:'blogUser/information',
        params:{
            userId
        }
    })
}


//修改用户信息
function update(data:User):Promise<R<string>>{
    return  request({
        method:"POST",
        url:'blogUser/update',
        data
    })
}

//消息中心侧边栏红点
function messageAsStatus(userId:number):Promise<R<{ "attention": boolean, "like": boolean, "privateletter": boolean, "comment": boolean, "articlepush": boolean }>>{
    return  request({
        method:"get",
        url:'blogUser/messageAsStatus',
       params:{
            userId
       }
    })
}

export {
    informationView,
    update,
    messageAsStatus
}