import {Time} from "./Time";
import {Art} from "./art";
import {User} from "./user";

type Attention = Time & {
    id?: number;

    /**
     * 用户id
     */
    userId?: number;

    /**
     * 关注的用户id
     */
    attentionUserId?: number;

    user?: User;

    attentionUser?: User;

}

export {
    Attention
}