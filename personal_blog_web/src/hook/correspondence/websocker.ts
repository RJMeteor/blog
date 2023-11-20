import {User} from "../../define/user";

class WebsocketUtil{
    static webSocket: WebSocket;

    constructor() {
        const user:User = JSON.parse(localStorage.getItem("user")as string)
        if (!WebsocketUtil.webSocket) {
            WebsocketUtil.webSocket = new WebSocket(`ws://${import.meta.env.BLOG_WebSocket_URi}websocket/${user.id}`)
            WebsocketUtil.webSocket.onclose = this.#close
            WebsocketUtil.webSocket.onopen = this.#open
            WebsocketUtil.webSocket.onerror = this.#onerror
        }
    }

    /*
    * 连接成功
    * */
    #open(event:Event) {
        console.log("连接成功")

    }

    /*
    * 错误
    * */
    #onerror(event:Event) {


    }

    /*
    * 关闭连接
    * */
    #close(event:CloseEvent) {


    }
}

export {
    WebsocketUtil
}

