import {Time} from "./Time";
import {Art} from "./art";
import {User} from "./user";

type Fans = Time & {
    id: number;
    /**
     * 用户id
     */
    userId: number;

    /**
     * 粉丝的用户id
     */
    fansUserId: number;


    user: User;

    fansUser: User;


}
export {
    Fans
}