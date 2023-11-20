import request from '../../config/axios.config'
import {Login, Regist} from "../../define/login.request";
import {User, UserInfo} from "../../define/user";
import {R} from "../../define/r";
import {Fans} from "../../define/fans";
import {Attention} from "../../define/attention";

//粉丝列表
function fansAsList(userId:number):Promise<R<Array<Fans>>>{
    return  request({
        method:"get",
        url:'blogFans/list',
        params:{
            userId
        }
    })
}


//关注列表
function attentionAsList(userId:number):Promise<R<Array<Attention>>>{
    return  request({
        method:"get",
        url:'blogAttention/list',
        params:{
            userId
        }
    })
}

//关注
function attentionRequest(data:Attention):Promise<R<Array<Attention>>>{
    return  request({
        method:"put",
        url:'blogAttention/attention',
        data,
        headers:{
            "Content-Type":"application/json"
        }
    })
}

export {
    fansAsList,
    attentionAsList,
    attentionRequest
}