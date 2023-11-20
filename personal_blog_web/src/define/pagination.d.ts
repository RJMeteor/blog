interface Pagination<T> {
    //总数
    total:number;
    //当前页
    current:number,
    //每页长度
    size:number
    //结果集
    records:Array<T>
}

export {
    Pagination
}