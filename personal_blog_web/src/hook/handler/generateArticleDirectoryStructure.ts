import {HeadList} from "md-editor-v3";
import {reactive, watch, ref, Ref, nextTick} from "vue"
import {useIntersectionObserver} from "@vueuse/core"


/*
* @param activeColor 目录激活元素
* @param listenElementScorll 滑动监听元素，目录颜色和位置切换
* @param htmlToDirectoryElement 根据元素区域的到目录结构
* @param personElement 包含元素区域的父元素
* */
type requiredParamsType = {
    activeColor: string,
    listenElementScorll: Element | undefined,
    htmlToDirectoryElement: Element | undefined,
    personElement: Element | undefined
}
let requiredParams: requiredParamsType = {
    activeColor: "red",
    listenElementScorll: undefined,
    htmlToDirectoryElement: undefined,
    personElement: undefined
}

interface Type {
    titleName: string, //目录名
    element: Element, //自身
    personElement: Element, //父亲
    index: number, //自身下标
    childers: Array<Type>  //子元素
    activeColor: string,
    leval: number //层级
    key: number //元素唯一的标识
}

const storeRenderingDirectory = ref<Array<Type>>([])
const directoryLevelList = ref<Array<Element>>([])
const leval = ["h1", "h2", "h3", "h4", "h5", "h6"]

let timeout: any = null
//防抖，只请求最后一次操作
const testFun = (fun: Function, time = 10) => {
    if (timeout) {
        clearTimeout(timeout) // 规定时间内若定时器存在则清除
    }
    timeout = setTimeout(() => {
        fun() // 调用接口
    }, time);
}

function scr() {
    // @ts-ignore
    testFun(moveDirectoryListener.bind({scrollTop: this.scrollTop}))
}

function observer(target: Element) {
    // @ts-ignore
    document.getElementById("readerComment").addEventListener("scroll", scr)
}


function moveDirectoryListener() {
    for (let directoryLevelListKey in directoryLevelList.value) {
        // @ts-ignore
        let windowTop: number = this.scrollTop - directoryLevelList.value[+directoryLevelListKey].offsetTop
        if (windowTop >= 0 && windowTop < 100) {
            endEntry.value = directoryLevelList.value[directoryLevelListKey]
        } else {
            // @ts-ignore
            if (this.scrollTop == 0) {
                endEntry.value = directoryLevelList.value[0]
            }
        }
    }
}

const endEntry = ref<Element>()
//监听激活的元素变化，改变样式
watch(endEntry, (value, oldValue) => {
    if (value != oldValue) {
        activeColorChange("#4CAF50", value as Element, "", oldValue as Element, storeRenderingDirectory.value)
    }
})


function activeColorChange(newColor: string, newElement: Element, oldColor: string, oldElement: Element, forEachEle: Array<Type>) {
    for (let type of forEachEle) {
        const isArray = type.childers.length > 0
        if (isArray) {
            // @ts-ignore
            activeColorChange(newColor, newElement, oldColor, oldElement, type.childers)
        }
        if (type.element == newElement) {
            type.activeColor = newColor
            const ele = document.querySelector(`.dirTitleList[data-key=key${type.key}]`)
            const scroller = document.querySelectorAll(".n-scrollbar-container")[2]
            // @ts-ignore
            if (document.body.clientHeight <= ((ele?.offsetTop + 180) - scroller.scrollTop) || ((ele.offsetTop + 180) - scroller.scrollTop) < 180) {
                ele?.scrollIntoView({
                    block: "center",
                    behavior: "smooth"
                })
            }
        }
        if (type.element == oldElement) {
            type.activeColor = oldColor
        }
    }
}


function equealPerson(ele: Element): Element {
    return <Element>(ele.parentElement?.nodeName?.toLowerCase() == "article" ? ele.previousElementSibling : equealPerson(<Element>ele.parentElement))
}

const keyCount = ref<number>(0)

//划分目录层级
function directoryLevel(target: Element, forEachList: Array<Element>, isChilder: boolean) {
    const personal = equealPerson(target)
    for (let ele in forEachList) {
        const isArray = Object.prototype.toString.call(forEachList[ele]).toLowerCase().includes("array")
        const isEqueal = forEachList[ele] == personal
        if (isEqueal) {
            const person = forEachList[ele]
            const observers = storeRenderingDirectory.value[ele]
            ++keyCount.value
            storeRenderingDirectory.value[ele].childers.push({
                titleName: target.textContent?.trim() as string,
                element: target,
                personElement: <Element>person,
                index: +ele,
                childers: [],
                activeColor: "",
                leval: observers.leval + 1,
                key: keyCount.value
            })
            observer(target)
        }
        if (isArray) {
            directoryLevel(target, <any>forEachList[ele], true)
        }
    }
}


function generateDirectory() {
    directoryLevelList.value = []
    storeRenderingDirectory.value = []
    let htmlStructure = document.getElementsByClassName("wenzhangComment")[0];
    // let htmlStructure = document.getElementsByTagName("article")[0];
    let elementNodeListOf = Array.from(htmlStructure.querySelectorAll("h1,h2,h3,h4,h5,h6"));
    let childList: Array<Element> = []
    for (let ele in elementNodeListOf) {
        directoryLevelList.value.push(elementNodeListOf[ele])
        if (elementNodeListOf[ele].parentElement?.nodeName.toLowerCase() == "article") {
            ++keyCount.value
            if(+ele==0){
                endEntry.value = elementNodeListOf[ele]
            }
            storeRenderingDirectory.value.push({
                titleName: elementNodeListOf[ele].textContent?.trim() as string,
                element: elementNodeListOf[ele],
                personElement: <Element>elementNodeListOf[ele].parentElement,
                index: +ele,
                childers: [],
                activeColor: +ele == 0 ? "#4CAF50" : "",
                leval: 0,
                key: keyCount.value
            })
            observer(elementNodeListOf[ele])
        } else {
            directoryLevel(elementNodeListOf[ele], directoryLevelList.value, false)
        }
    }
}

function exportGenerateDirectory() {
    nextTick(function () {
        generateDirectory()
    })
}

export {
    exportGenerateDirectory,
    storeRenderingDirectory,
    endEntry
}