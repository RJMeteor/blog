<script setup lang="ts">
    import {onMounted} from "vue"
    import * as echarts from 'echarts';

    onMounted(function () {
        // 基于准备好的dom，初始化echarts实例
        var fanGrowthData = echarts.init(document.getElementById('fanGrowthData'));
        // 绘制图表
        fanGrowthData.setOption({
            title: {
                text: '粉丝增长数据'
            },
            tooltip: {},
            xAxis: {
                type: 'category',
                data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    data: [820, 932, 901, 934, 1290, 1330, 1320],
                    type: 'line',
                    smooth: true
                }
            ]
        });


        // 基于准备好的dom，初始化echarts实例
        var browseGrowthData = echarts.init(document.getElementById('browseGrowthData'));
        // 绘制图表
        browseGrowthData.setOption({
            title: {
                text: '浏览增长数据'
            },
            tooltip: {},
            xAxis: {
                type: 'category',
                data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    data: [820, 932, 901, 934, 1290, 1330, 1320],
                    type: 'line',
                    smooth: true
                }
            ]
        });
    })

</script>

<template>
    <n-scrollbar>
    <div id="dataanalysis">
            <div class="echart" id="fanGrowthData"></div>
            <div class="echart" id="browseGrowthData"></div>
    </div>
    </n-scrollbar>
</template>

<style lang="scss" scoped>
    #dataanalysis {
        width: calc(100% - 20px);
        height: calc(100vh - 20px);
        padding: 10px;

        .echart {
            width: 100%;
            height: 400px;
        }
    }
</style>
